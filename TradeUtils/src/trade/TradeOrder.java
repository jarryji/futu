package trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.client.FutuClient;
import client.model.account.HKOrderItem;
import client.model.order.SetOrderStatusParam;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceResult;
import client.util.TimeUtils;

/**
 * 处理单个订单通讯
 * 
 * @author jarry
 */
public class TradeOrder extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TradeOrder.class);

	public final static int QUEUE_STATUS_CREATE				=	0;	//创建
	public final static int QUEUE_STATUS_QUEUE				=	1;	//正在列队等待发送
	public final static int QUEUE_STATUS_SENT				=	2;	//已经发送
	public final static int QUEUE_STATUS_LOCAL_CONFIRM		=	3;	//确认发送
	public final static int QUEUE_STATUS_SYNC_NOT_PLACE		=	4;	//超时后,确认订单未成功
	public final static int QUEUE_STATUS_WAIT_DEAL			=	5;	//确认发送
	public final static int QUEUE_STATUS_PARTIAL_DEAL		=	6;	//部份交
	public final static int QUEUE_STATUS_ALL_DEAL			=	7;	//全部成交
	
	public final static int QUEUE_STATUS_CANCEL				=	11;	//取消 主动,不需要重试
	public final static int QUEUE_STATUS_FAILED				=	12;	//失败 被动, 需要重试
	
	public final static int QUEUE_STATUS_UNKNOW				=	99;	//未知

	public final static int ORDER_RETRY_TIMES				=	6;		//重试次数
	public final static int ORDER_RETRY_INTERVAL			=	500;	//重试间隔ms
	public final static int ORDER_RETRY_TIMEOUT				=	3000;	//超时重试ms
	
	private String stockCode;							//证券代码
	private String market;								//市场
	private String cookie = "0";						//识别码

	private String orderSide;							//ORDER_SIDE_BUY, ORDER_SIDE_SELL
	private String price;								//单价 (厘 1000 = 1.000)
	private String qty;									//数理
	
	private String localId = "0";
	private String orderId = "0";
	
	private HKOrderItem orderItem = null;				//返回的订单信息

	private int queueStatus = QUEUE_STATUS_UNKNOW;		//状态
	private int retryCount = ORDER_RETRY_TIMES;			//重试次数
	private long afterTradeTime		=	0;				//在这之后的时间交易
	private long submitTime			=	0;				//发送时间
	private long timeoutTime;							//未收到返回包, 的超时重试ms
	
	private int waitTime = 100;
	private boolean run = true;
	
	private TradeManager tradeManager = TradeManager.getInstance();
	private FutuClient futuClient = tradeManager.getFutuClient();
	private ObserableTradeOrder obserable = new ObserableTradeOrder();
	
	@Override
	public void run()
	{
		run = true;
		logger.debug("StockCode: " + stockCode + " started!");
		while (run) {
			try {
				startTrade();
				sleep(waitTime);
			} catch (InterruptedException e) {
				logger.debug(e.getMessage());
				run = false;
			}
		}
		logger.debug("StockCode: " + stockCode + " Interrupted!");
	}
	
	private void delayRetry(int timeout)
	{
		waitTime = timeout;
	}

	public boolean placeOrder(int price, int qty) {
		String message = "新下订单 代码: " + getStockCode() + " 价格: " + getPrice() + " 数量: " + getQty();
		String cookie = futuClient.getNewCookie();
		
		setQueueStatus(TradeOrder.QUEUE_STATUS_QUEUE);
		setRetryCount(TradeOrder.ORDER_RETRY_TIMES);
		setPrice(price + "");
		setQty(qty + "");
		setCookie(cookie);
		updateSubmitTime();
		updateTimeoutTime();
		boolean ret;
		
		if((ret = futuClient.placeOrder(cookie, getMarket(), getOrderSide(), getStockCode(), getPrice(), getQty())))
		{
			setQueueStatus(TradeOrder.QUEUE_STATUS_SENT);
			obserable.setTradeMessage(message + "发送成功");
			obserable.setShortMessage("添加订单");
		} else {
			obserable.setTradeMessage(message + "发送失败");
			obserable.setShortMessage("添加失败");
		}
		obserable.onUpdate();
		
		start();
		return ret;
	}
	
	public boolean changeOrder(int price, int qty)
	{
		String message = "修改订单 代码: " + getStockCode() + " 价格: " + getPrice() + " 数量: " + getQty();
		boolean ret = false;
		
		if(queueStatus == TradeOrder.QUEUE_STATUS_WAIT_DEAL)
		{
			String cookie = futuClient.getNewCookie();
			setQueueStatus(TradeOrder.QUEUE_STATUS_SENT);
			setRetryCount(TradeOrder.ORDER_RETRY_TIMES);
			setPrice(price + "");
			setQty(qty + "");
			setCookie(cookie);
			updateSubmitTime();
			updateTimeoutTime();
			if((ret = futuClient.changeOrder(cookie, getMarket(), getLocalId(), getOrderId(), getPrice(), getQty())))
			{
				obserable.setTradeMessage(message + "发送成功");
				obserable.setShortMessage("修改订单");
			} else {
				obserable.setTradeMessage(message + "发送失败");
				obserable.setShortMessage("修改失败");
			}
			obserable.onUpdate();
		}
		return ret;
	}
	
	public boolean deleteOrder()
	{
		String message = "删除订单 代码: " + getStockCode();
		if(getLocalId().isEmpty() || getOrderId().isEmpty())
		{
			logger.debug("getLocalId().isEmpty() || getOrderId().isEmpty()");
			return false;
		}

		boolean ret = false;
		
		String cookie = futuClient.getNewCookie();
		setQueueStatus(TradeOrder.QUEUE_STATUS_SENT);
		setRetryCount(TradeOrder.ORDER_RETRY_TIMES);
		setCookie(cookie);
		updateSubmitTime();
		updateTimeoutTime();
		
		if((ret = futuClient.setOrderStatus(cookie, getStockCode(), getLocalId(), getOrderId(), SetOrderStatusParam.ORDER_TYPE_CANCEL)))
		{
			obserable.setTradeMessage(message + "发送成功");
			obserable.setShortMessage("删除订单");
		} else {
			obserable.setTradeMessage(message + "发送失败");
			obserable.setShortMessage("删除失败");
		}
		obserable.onUpdate();
		return ret;
	}
	
	private void startTrade()
	{
		if(queueStatus == QUEUE_STATUS_CREATE)
		{
			return;
		}
		//是否有未完成的父订单, 一班是取消订单命令
		if(getParentOrder() != null)
		{
			if(getParentOrder().isOrderSuceed() != true)
				return;
		}

		if(isOrderSuceed())
		{
			//订单成功
			//queueOrderList.remove(order);
			logger.info("TradeOrder suceed" + this.getOrderId());
			this.interrupt();
		} else if(isOrderFailed())
		{
			//失败
			//queueOrderList.remove(order);
			logger.info("TradeOrder failed");
			this.interrupt();
		} else if(queueStatus == TradeOrder.QUEUE_STATUS_QUEUE)
		{
			//发送了订单, 但是超时, 则同步订单及执仓
			if(getRetryCount() > 0 || getTimeoutTime() < TimeUtils.getCurrentTimestamp())
			{
				String cookie = futuClient.getNewCookie();
				setCookie(cookie);
				if(futuClient.placeOrder(cookie, getMarket(), getOrderSide(), getStockCode(), getPrice(), getQty()))
				{
					setQueueStatus(TradeOrder.QUEUE_STATUS_SENT);
				}
				delayRetry(50);

				logger.info("TradeOrder QUEUE_STATUS_QUEUE" + this.getOrderId());
			}
		} else if(queueStatus == TradeOrder.QUEUE_STATUS_SENT)
		{
			//发送了订单, 但是超时, 则同步订单及执仓
			if(getRetryCount() > 0 || getTimeoutTime() < TimeUtils.getCurrentTimestamp())
			{
				//发送同步信息
				tradeManager.synchronizeOrders();
				updateRetryCount();
				delayRetry(50);
				logger.info("TradeOrder QUEUE_STATUS_SENT" + this.getOrderId());
			}
		} else if(queueStatus == TradeOrder.QUEUE_STATUS_LOCAL_CONFIRM)
		{
			//发送了订单Futu插件已经收到, 但是服务器状态没有变化
			if(getRetryCount() > 0 || getTimeoutTime() < TimeUtils.getCurrentTimestamp())
			{
				//发送同步信息
				tradeManager.synchronizeOrders();
				updateRetryCount();
				delayRetry(50);
				logger.info("TradeOrder QUEUE_STATUS_LOCAL_CONFIRM" + this.getOrderId());
			}
		} else if(queueStatus == TradeOrder.QUEUE_STATUS_SYNC_NOT_PLACE)
		{
			//如果同步服务器定单后, 发现订单超时, 重新发送订单
			if(getRetryCount() > 0 || getTimeoutTime() < TimeUtils.getCurrentTimestamp())
			{
				if(placeOrder(Integer.parseInt(getPrice()), Integer.parseInt(getQty())))
				{
				}
				delayRetry(100);
				logger.info("TradeOrder QUEUE_STATUS_SYNC_NOT_PLACE" + this.getOrderId());
			}
		} else if(isWaitDeal())
		{
			this.interrupt();
			logger.info("TradeOrder isWaitDeal" + this.getOrderId());
		}
		
		if(getRetryCount() <= 0 || getTimeoutTime() < TimeUtils.getCurrentTimestamp())
		{
			this.interrupt();
			logger.info("TradeOrder timeout" + this.getOrderId());
			setQueueStatus(TradeOrder.QUEUE_STATUS_FAILED);
			obserable.setShortMessage("发送订单超时");
			obserable.onUpdate();
		}
	}

	public String getStockCode() {
		return stockCode;
	}
	
	private TradeOrder parentOrder = null;
	
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getOrderSide() {
		return orderSide;
	}
	public void setOrderSide(String orderSide) {
		this.orderSide = orderSide;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public HKOrderItem getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(HKOrderItem orderItem) {
		this.orderItem = orderItem;
	}
	public int getQueueStatus() {
		return queueStatus;
	}
	public void setQueueStatus(int queueStatus) {
		this.queueStatus = queueStatus;
	}
	public long getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}
	public long getTimeoutTime() {
		return timeoutTime;
	}
	public void setTimeoutTime(long timeoutTime) {
		this.timeoutTime = timeoutTime;
	}
	public int getRetryCount() {
		return retryCount;
	}
	
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	
	public void updateRetryCount() {
		this.retryCount--;
	}
	
	public boolean isOrderSuceed()
	{
		if(queueStatus == QUEUE_STATUS_WAIT_DEAL || queueStatus == QUEUE_STATUS_PARTIAL_DEAL || queueStatus == QUEUE_STATUS_ALL_DEAL)
			return true;
		else
			return false;
	}
	
	public boolean isOrderCanceled()
	{
		return queueStatus == QUEUE_STATUS_CANCEL;
	}
	
	/**
	 * 已经重试完3次, 及状态 = QUEUE_STATUS_FAILED
	 * @return
	 */
	public boolean isOrderFailed()
	{
		if(retryCount == 0 && queueStatus == QUEUE_STATUS_FAILED)
			return true;
		else
			return false;
	}
	
	public TradeOrder getParentOrder() {
		return parentOrder;
	}
	
	public void setParentOrder(TradeOrder parentOrder) {
		this.parentOrder = parentOrder;
	}
	
	public void updateTimeoutTime() {
		 setTimeoutTime(submitTime + ORDER_RETRY_TIMEOUT);
	}
	public void updateSubmitTime() {
		submitTime = TimeUtils.getCurrentTimestamp();
	}
	public String getLocalId() {
		return localId;
	}
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	//正在处理中
	public boolean isProccessing()
	{
		return (queueStatus == QUEUE_STATUS_QUEUE || queueStatus == QUEUE_STATUS_SENT || queueStatus == QUEUE_STATUS_LOCAL_CONFIRM);
	}
	
	//定单成功,但是没有完成
	public boolean isWaitDeal()
	{
		return (queueStatus == QUEUE_STATUS_WAIT_DEAL);
	}
	
	//未成功,或被取消
	public boolean isFailed()
	{
		return (queueStatus == QUEUE_STATUS_FAILED || queueStatus == QUEUE_STATUS_CANCEL);
	}
	
	//部份成功
	public boolean isPartialDeal()
	{
		return (queueStatus == QUEUE_STATUS_PARTIAL_DEAL);
	}
}
