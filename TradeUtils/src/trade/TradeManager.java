package trade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.client.FutuClient;
import client.model.ErrorCode;
import client.model.account.HKOrderItem;
import client.model.account.QueryAccountInfoResult;
import client.model.account.QueryOrderListResult;
import client.model.account.QueryStockListResult;
import client.model.base.BasePacket;
import client.model.observer.ObserableAccountInfo;
import client.model.observer.ObserableChangeOrder;
import client.model.observer.ObserableGearPrice;
import client.model.observer.ObserableOrderList;
import client.model.observer.ObserablePlaceOrder;
import client.model.observer.ObserableSetOrderStatus;
import client.model.observer.ObserableStockList;
import client.model.observer.ObserableStockPrice;
import client.model.order.ChangeOrderResult;
import client.model.order.PlaceOrderParam;
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusResult;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceResult;
import client.util.TimeUtils;

public class TradeManager extends Thread implements Observer {
	private static final Logger logger = LoggerFactory.getLogger(TradeManager.class);
	private FutuClient futuClient = null;
	private List<TradeOrder> queueOrderList = new ArrayList<TradeOrder>();
	private QueryAccountInfoResult accountInfo = null;
	private QueryStockListResult stockList = null;
	private QueryOrderListResult orderList = null;
	private static TradeManager tradeManager = null;

	private String market = BasePacket.MARKET_HK;
	private boolean run = true;
	private int interval = 1500;
	private long nextTime = TimeUtils.getCurrentTimestamp() + interval;
	
	public TradeManager()
	{
		interval = FutuClient.getConfig().getSyncInterval();
		if(interval < 500) interval = 500;
	}
	
	public static TradeManager getInstance()
	{
		if(tradeManager == null)
		{
			tradeManager = new TradeManager();
			tradeManager.start();
		}
		
		return tradeManager;
	}
	
	@Override
	public void run()
	{
		run = true;
		while (run) {
			try {
				if(nextTime - TimeUtils.getCurrentTimestamp() <=0)
				{
					synchronizeOrders();
				} else {
					sleep(nextTime - TimeUtils.getCurrentTimestamp());
				}
			} catch (InterruptedException e) {
				logger.debug(e.getMessage());
				run = false;
			}
		}
	}
	
	 //多少时间后重试一次
	protected void delayRetry(int timeout) {
		final Object o = this;
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (o) {
					o.notify();
				}
			}
		}, new Date(System.currentTimeMillis() + timeout));
	}

	
	/**
	 * 同步订单,执仓
	 */
	public void synchronizeOrders()
	{
		if(futuClient == null) return;
		futuClient.getOrderList(futuClient.getNewCookie(), market);
		futuClient.getStockList(futuClient.getNewCookie(), market);
		futuClient.getAccountInfo(futuClient.getNewCookie());
		nextTime = TimeUtils.getCurrentTimestamp() + interval;
	}

	/**
	 * 添加订单任务
	 * 
	 * @param market
	 * @param orderSide
	 * @param stockCode
	 * @param price
	 * @param qty
	 */
	public TradeOrder createOrder(String market, String orderSide, String stockCode, int price, int qty)
	{
		synchronized(queueOrderList)
		{
			TradeOrder order = new TradeOrder();
			order.setMarket(market);
			order.setOrderSide(orderSide);
			order.setStockCode(stockCode);
			order.setPrice(price + "");
			order.setQty(qty + "");
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_CREATE);
			order.setRetryCount(TradeOrder.ORDER_RETRY_TIMES);
			order.updateSubmitTime();
			order.updateTimeoutTime();
			queueOrderList.add(order);
			return order;
		}
	}
	
	/**
	 * 
	 * @param tradeOrder
	 * @return
	 */
	public boolean removeOrder(TradeOrder tradeOrder)
	{
		synchronized(queueOrderList)
		{
			for(TradeOrder order: queueOrderList)
			{
				if(order.equals(order))
				{
					queueOrderList.remove(order);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	public FutuClient getFutuClient() {
		return futuClient;
	}

	public void setFutuClient(FutuClient futuClient) {
		this.futuClient = futuClient;
	}

	private String getNewCookie() {
		return futuClient.getNewCookie();
	}
	
	
	public QueryAccountInfoResult getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(QueryAccountInfoResult accountInfo) {
		this.accountInfo = accountInfo;
	}

	public QueryStockListResult getStockList() {
		return stockList;
	}

	public void setStockList(QueryStockListResult stockList) {
		this.stockList = stockList;
	}

	public QueryOrderListResult getOrderList() {
		return orderList;
	}

	public void setOrderList(QueryOrderListResult orderList) {
		this.orderList = orderList;
	}

	public TradeOrder getOrderByLocalId(String localId)
	{
		synchronized(queueOrderList)
		{
			for(TradeOrder order : queueOrderList)
			{
				if(order.getLocalId().compareTo(localId) == 0)
					return order;
			}
		}
		
		return null;
	}
	
	public TradeOrder getValidOrder(String stockCode)
	{
		synchronized(queueOrderList)
		{
			for(TradeOrder order : queueOrderList)
			{
				if(order.getStockCode().compareTo(stockCode) == 0)
				{
					if(order.isProccessing() || order.isWaitDeal() || order.isPartialDeal())
						return order;
				}
			}
		}
		
		return null;
	}

	public TradeOrder getOrderByCode(String stockCode)
	{
		synchronized(queueOrderList)
		{
			for(TradeOrder order : queueOrderList)
			{
				if(order.getStockCode().compareTo(stockCode) == 0)
					return order;
			}
		}
		
		return null;
	}
	
	public TradeOrder getOrderByCookie(String cookie)
	{
		synchronized(queueOrderList)
		{
			for(TradeOrder order : queueOrderList)
			{
				if(order.getCookie().compareTo(cookie) == 0)
					return order;
			}
		}
		
		return null;
	}
	
	public boolean isOrderExist(String stockCode)
	{
		TradeOrder order = getOrderByCode(stockCode);
		if(order == null)
			return false;
		else
			return true;
	}
	protected void onGearPriceResult(GearPriceResult gpr) {}
	protected void onStockPriceResult(StockPriceResult spr) {}
	protected void onPlaceOrderResult(PlaceOrderResult por)
	{
		if(Integer.parseInt(por.getSvrResult()) == ErrorCode.PROTO_ERR_NO_ERROR.getErrorCode())
		{
			TradeOrder order = getOrderByCookie(por.getCookie());
			if(order == null)
				return;
			order.setLocalId(por.getLocalID());
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_LOCAL_CONFIRM);
			synchronizeOrders();
		} else {
			logger.info("onPlaceOrderResult SvrResult: " + por.getSvrResult());
		}
	}
	
	/**
	 * 修改订单返回
	 * @param cor
	 */
	protected void onChangeOrderResult(ChangeOrderResult cor) {
		if(Integer.parseInt(cor.getSvrResult()) == ErrorCode.PROTO_ERR_NO_ERROR.getErrorCode())
		{
			TradeOrder order = getOrderByCode(cor.getLocalID());
			if(order == null)
				return;
			order.setOrderId(cor.getOrderID());
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_LOCAL_CONFIRM);
			synchronizeOrders();
		} else {
			logger.info("onChangeOrderResult SvrResult: " + cor.getSvrResult());
		}
	}
	
	/**
	 * 设置订单状态返回
	 * @param sosr
	 */
	protected void onSetOrderStatusResult(SetOrderStatusResult sosr) {
		if(Integer.parseInt(sosr.getSvrResult()) == ErrorCode.PROTO_ERR_NO_ERROR.getErrorCode())
		{
			TradeOrder order = getOrderByCode(sosr.getLocalID());
			if(order == null)
				return;
			order.setOrderId(sosr.getOrderID());
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_LOCAL_CONFIRM);
			synchronizeOrders();
		} else {
			logger.info("onSetOrderStatusResult SvrResult: " + sosr.getSvrResult());
		}
		
	}

	/**
	 * 执仓列表
	 * @param qslr
	 */
	protected void onStockListResult(QueryStockListResult qslr) {
		if(stockList == null)
		{
			stockList = qslr;
			return;
		}
		synchronized(stockList)
		{
			stockList = qslr;
		}
	}
	
	/**
	 * 账号统计信息
	 * @param qair
	 */
	protected void onAccountInfoResult(QueryAccountInfoResult qair) {
		if(accountInfo == null)
		{
			accountInfo = qair;
			return;
		}
		synchronized(accountInfo)
		{
			accountInfo = qair;
		}
	}

	/**
	 * 订单列表
	 * @param qosr
	 */
	protected void onOrderListResult(QueryOrderListResult qolr) {
		if(qolr == null) return;
		if(orderList == null) orderList = qolr;
		
		synchronized(orderList)
		{
			orderList = qolr;
			List<HKOrderItem> orderArray = orderList.getHKOrderArr();
			if(orderArray == null) return;
			
			for(HKOrderItem orderItem : orderArray)
			{
				if(orderItem.getOrderSide().compareTo(PlaceOrderParam.ORDER_SIDE_SELL) != 0) continue;
				boolean found = false;
				synchronized(queueOrderList)
				{
					for(TradeOrder order: queueOrderList)
					{
						if(Long.parseLong(order.getLocalId()) == Long.parseLong(orderItem.getLocalID()) || 
								Long.parseLong(order.getOrderId()) == Long.parseLong(orderItem.getOrderID()))
						{
							updateTradeOrder(order,orderItem);
							found = true;
							break;
						}
//						else if(order.getStockCode().compareTo(orderItem.getStockCode()) == 0)
//						{
//							updateTradeOrder(order,orderItem);
//							found = true;
//							break;
//						}
					}
					
					if(found == false)
					{
						TradeOrder order = new TradeOrder();
						order.setMarket(market);
						order.setOrderSide(orderItem.getOrderSide());
						order.setStockCode(orderItem.getStockCode());
						order.setPrice(orderItem.getPrice());
						order.setQty(orderItem.getQty());
						order.setRetryCount(TradeOrder.ORDER_RETRY_TIMES);
						order.setLocalId(orderItem.getLocalID());
						order.setOrderId(orderItem.getOrderID());
						order.updateSubmitTime();
						order.updateTimeoutTime();
						updateTradeOrder(order,orderItem);
						queueOrderList.add(order);
					}
				}
			}
		}
	}
	
	private void updateTradeOrder(TradeOrder order, HKOrderItem orderItem) {
		
		int orderItemStatus = Integer.parseInt(orderItem.getStatus());
		switch (orderItemStatus)
		{
		case HKOrderItem.ORDER_STATUS_LOCAL_SENT:
		case HKOrderItem.ORDER_STATUS_LOCAL_SENT_WAIT_RET:
		case HKOrderItem.ORDER_STATUS_WAIT_PROCESS:
		case HKOrderItem.ORDER_STATUS_ORDER_WAIT_OPEN:
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_LOCAL_CONFIRM);
		break;
		case HKOrderItem.ORDER_STATUS_ALL_DEAL:
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_ALL_DEAL);
			break;
		case HKOrderItem.ORDER_STATUS_PARTIAL_DEAL:
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_PARTIAL_DEAL);
			break;
		case HKOrderItem.ORDER_STATUS_WAIT_DEAL:
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_WAIT_DEAL);
			break;
		case HKOrderItem.ORDER_STATUS_ORDER_CANCELED:
		case HKOrderItem.ORDER_STATUS_ORDER_DELETED:
		case HKOrderItem.ORDER_STATUS_ORDER_DISABLED:
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_CANCEL);
			break;
		case HKOrderItem.ORDER_STATUS_ORDER_FAILED:
			order.setQueueStatus(TradeOrder.QUEUE_STATUS_FAILED);
			break;
		}

		if(orderItem.getQty().compareTo(order.getQty()) != 0)
			order.setQty(orderItem.getQty());
		
		if(orderItem.getPrice().compareTo(order.getPrice()) != 0)
			order.setPrice(orderItem.getPrice());
		
		if(order.getQueueStatus() != TradeOrder.QUEUE_STATUS_CANCEL)
		{
		//	logger.info("update order :" + order.getLocalId() + " orderId:" + order.getOrderId() + "status :" + order.getQueueStatus() + " : ", orderItemStatus);
		}
		
	}


	@Override
	public void update(Observable o, Object arg) {
		
		synchronized (this)
		{
			if(ObserableGearPrice.class.isInstance(o))
			{
				//处理更新摆盘数据
				ObserableGearPrice ogp = (ObserableGearPrice)o;
				synchronized (ogp)
				{
					GearPriceResult gpr = ogp.getGearPriceResult();
					onGearPriceResult(gpr);
				}
			} else if(ObserableStockPrice.class.isInstance(o))
			{
				//处理更新股票数据
				ObserableStockPrice osp = (ObserableStockPrice)o;
				synchronized (osp)
				{
					StockPriceResult spr = osp.getStockPriceResult();
					onStockPriceResult(spr);
				}
				
			} else if(ObserablePlaceOrder.class.isInstance(o))
			{
				ObserablePlaceOrder obserable = (ObserablePlaceOrder)o;
				synchronized (obserable)
				{
					PlaceOrderResult por = obserable.getPlaceOrderResult();
					onPlaceOrderResult(por);
				}
			} else if(ObserableChangeOrder.class.isInstance(o))
			{
				ObserableChangeOrder obserable = (ObserableChangeOrder)o;
				synchronized (obserable)
				{

					ChangeOrderResult cor = obserable.getChangeOrderResult();
					onChangeOrderResult(cor);
				}
			} else if(ObserableSetOrderStatus.class.isInstance(o))
			{
				ObserableSetOrderStatus obserable = (ObserableSetOrderStatus)o;
				synchronized (obserable)
				{
					SetOrderStatusResult sosr = obserable.getSetOrderStatusResult();
					onSetOrderStatusResult(sosr);
				}
			} else if(ObserableAccountInfo.class.isInstance(o))
			{
				ObserableAccountInfo obserable = (ObserableAccountInfo)o;
				synchronized (obserable)
				{
					QueryAccountInfoResult qair = obserable.getQueryAccountInfoResult();
					onAccountInfoResult(qair);
				}
			} else if(ObserableOrderList.class.isInstance(o))
			{
				ObserableOrderList obserable = (ObserableOrderList)o;
				synchronized (obserable)
				{
					QueryOrderListResult qolr = obserable.getQueryOrderListResult();
					onOrderListResult(qolr);
				}
			} else if(ObserableStockList.class.isInstance(o))
			{
				ObserableStockList obserable = (ObserableStockList)o;
				synchronized (obserable)
				{
					QueryStockListResult qslr = obserable.getQueryStockListResult();
					onStockListResult(qslr);
				}
			}
		}
	}

	public boolean readyTrade() {
		if(accountInfo == null || stockList == null || orderList == null)
		{
			logger.info("accountInfo == null || stockList == null || orderList == null....");
			synchronizeOrders();
			return false;
		} else
			return true;
	}

}
