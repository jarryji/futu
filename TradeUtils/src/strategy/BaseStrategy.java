package strategy;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.mina.core.future.ConnectFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trade.ObserableTradeOrder;
import trade.TradeManager;
import trade.TradeOrder;
import client.client.FutuClient;
import client.model.account.HKOrderItem;
import client.model.account.HKPositionItem;
import client.model.account.QueryAccountInfoResult;
import client.model.account.QueryOrderListResult;
import client.model.account.QueryStockListResult;
import client.model.account.UnlockTradeResult;
import client.model.observer.ObserableAccountInfo;
import client.model.observer.ObserableChangeOrder;
import client.model.observer.ObserableGearPrice;
import client.model.observer.ObserableOrderList;
import client.model.observer.ObserablePlaceOrder;
import client.model.observer.ObserableSetOrderStatus;
import client.model.observer.ObserableStockList;
import client.model.observer.ObserableStockPrice;
import client.model.observer.ObserableUnlockTrade;
import client.model.order.ChangeOrderResult;
import client.model.order.PlaceOrderParam;
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusResult;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceResult;

public abstract class BaseStrategy extends Thread implements Observer {
	protected static Logger logger = LoggerFactory.getLogger(BaseStrategy.class);
	protected String StockCode = null;
	protected String Market = null;
	protected int costPrice;						//成本 (单位:厘 0.001 * 1000)
	protected int curPrice;							//现价 (单位:厘 0.001 * 1000)
	protected int tradePrice = 0;					//最近一次交易价格
	protected int tradeQty = 0;						//最近一次交易数量
	protected int holdVol;							//执有
	protected int lossLimit;						//跌落触发价(价格子)
	protected int updateCount = 0;
	protected boolean enabled = false;
	protected boolean forceCheck = true;			//订单价格或数量被其他终端修改后, 需要重新检查, 并下单
	protected int pollInterval = 1000;				//poll speed
	
	protected FutuClient futuClient = null;

	protected GearPriceResult gearPrice = null;			//摆盘数据
	protected StockPriceResult stockPrice = null;		//报价数据
	protected HKPositionItem hkPositionItem = null;		//执仓数据
	
	protected TradeManager tradeManager = TradeManager.getInstance();
	protected TradeOrder tradeOrder = null;

	protected abstract void onPlaceOrderResult(PlaceOrderResult por);
	protected abstract void onChangeOrderResult(ChangeOrderResult cor);
	protected abstract void onSetOrderStatusResult(SetOrderStatusResult sosr);

	protected abstract void onAccountInfoResult(QueryAccountInfoResult qair);
	protected abstract void onOrderListResult(QueryOrderListResult qosr);
	protected abstract void onStockListResult(QueryStockListResult qslr);
	
	protected abstract void onUnlockTradeResult(UnlockTradeResult utr);
	
	protected abstract boolean judge();
	
	public BaseStrategy()
	{
		pollInterval = FutuClient.getConfig().getPollInterval();
		if(pollInterval < 500) pollInterval = 500;
	}
	
	public FutuClient getFutuClient() {
		return futuClient;
	}

	public void setFutuClient(FutuClient futuClient) {
		this.futuClient = futuClient;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean updateGearPrice(GearPriceResult gpr)
	{
		if(gearPrice == null)
		{
			gearPrice = gpr;
		}
		else
		{
			//TODO: 需要修改 FT_PLUGIN, 添加报价时间
			if(gearPrice.compareTo(gpr) == 0)
				return false;
			
			//if(gearPrice.get)
			gearPrice = gpr;
		}
		return true;
	}

	public boolean updateStockPrice(StockPriceResult spr)
	{
		//需要执仓信息
		if(hkPositionItem == null) return false;
		
		//更新股价, 如有变化
		if(stockPrice == null) stockPrice = spr;
		else
		{
			logger.info("updateStockPrice: stock={}, newPrice={}, oldPrice={}",spr.getStockCode(),spr.getCur(),stockPrice.getCur());
			
			if(Integer.parseInt(spr.getCur()) != Integer.parseInt(stockPrice.getCur()))
			{
				logger.info("Old Cur: " + stockPrice.getCur() + " New Cur: " + spr.getCur());
				stockPrice = spr;
			} else {
				return false;
			}
		}

		this.curPrice = Integer.parseInt(spr.getCur());
		
		return true;
	}	
	
	public String getStockCode() {
		return StockCode;
	}
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}
	public String getMarket() {
		return Market;
	}
	public void setMarket(String market) {
		Market = market;
	}
	public int getHoldVol() {
		return holdVol;
	}
	public void setHoldVol(int holdVol) {
		this.holdVol = holdVol;
	}
	public int getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}
	public int getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(int curPrice) {
		this.curPrice = curPrice;
	}
	public int getLossLimit() {
		return lossLimit;
	}
	public void setLossLimit(int lossLimit) {
		this.lossLimit = lossLimit;
	}

	protected void onGearPriceResult(GearPriceResult gpr) {
		if(updateGearPrice(gpr))
			this.notify();
	};
	protected void onStockPriceResult(StockPriceResult spr) {
		if(updateStockPrice(spr))
			this.notify();
	};
	
	/**
	 * 有新的数据,执行判断
	 */
	@Override
    public void run()
	 {
		final BaseStrategy baseStrategy = this;
		
		final Thread t = new Thread(){
			public void run() {
				while (!isInterrupted()) {
					try {
						if(baseStrategy.enabled == false)
						{
							sleep(750);
						} else {
							sleep(baseStrategy.pollInterval);
						}

						if(StockCode == null || Market == null || futuClient == null)
							continue;

						futuClient.getStockPrice(Market, StockCode);
						//futuClient.getGearList(Market, StockCode);
					} catch (InterruptedException e) {
						logger.info(e.getMessage());
					}
				}
				int i =0;
				i++;
			}
		};
		
		t.start();
		
			while (!isInterrupted()) {
				try {
					logger.debug("Wait...");
					synchronized (this)
					{
						wait();
					}

					if(this.enabled == false)
					{
						logger.debug("Disabled...");
						continue;
					}
					
					logger.debug("Execute judge....");
					if(judge())
					{
						logger.debug("triggered....");
					}
					//sleep(10);
				} catch (InterruptedException e) {
					logger.debug(e.getMessage());
				}
			
		}
    }

	 /**
	  * 
	  * @param price
	  * @param qty
	  * @return
	  */
	 protected boolean sellStock(int price,int qty)
	 {
		 logger.info("Triggered Sell SellStock, stock: " + this.StockCode + " Price: " + price + " Qty: " + qty);

		 tradeOrder = tradeManager.getValidOrder(getStockCode());
		 
		 if(tradeOrder == null)
		 {
			 if(qty == 0) return false;
			 tradeOrder = tradeManager.createOrder(Market, PlaceOrderParam.ORDER_SIDE_SELL, StockCode, price, qty);
			 tradeOrder.placeOrder(price, qty);
			 tradePrice = price;
			 tradeQty = qty;
		 } else if(tradeOrder.isProccessing())
		 {
			 delayRetry(2000);
			 return false;
		 } else if(tradeOrder.isWaitDeal())
		 {
			 if(Integer.parseInt(tradeOrder.getPrice()) != price)
			 {
				 if(qty == 0) qty = Integer.parseInt(tradeOrder.getQty());
				 logger.info("changeOrder " + StockCode + " price: " + price + " qty: " + qty);
				 tradeOrder.changeOrder(price, qty);
				 tradePrice = price;
				 tradeQty = qty;
			 } else {
				 //TODO: 进一步测试
				 tradePrice = Integer.parseInt(tradeOrder.getPrice());
				 tradeQty = Integer.parseInt(tradeOrder.getQty());
				 return false;
			 }
		 } else if(tradeOrder.isFailed()){
			 return false;
		 }

		 return true;
	 }

	 //多少时间后重试一次
	protected void delayRetry(int timeout) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (this) {
					this.notify();
				}
			}
		}, new Date(System.currentTimeMillis() + timeout));
	}

	 /**
	  * 观察到数据更新
	  * 
	  */
	@Override
	public void update(Observable o, Object arg) {
		
		if(enabled == false) return;

		synchronized (this)
		{
			if(ObserableGearPrice.class.isInstance(o))
			{
				//处理更新摆盘数据
				ObserableGearPrice ogp = (ObserableGearPrice)o;
				synchronized (ogp)
				{
					GearPriceResult gpr = ogp.getGearPriceResult();
					
					if(gpr.getStockCode().compareTo(this.StockCode) != 0) return;
					if(gpr.getMarket().compareTo(this.Market) != 0) return;
					
					onGearPriceResult(gpr);
				}
			} else if(ObserableStockPrice.class.isInstance(o))
			{
				//处理更新股票数据
				ObserableStockPrice osp = (ObserableStockPrice)o;
				synchronized (osp)
				{
					StockPriceResult spr = osp.getStockPriceResult();

					if(spr.getStockCode().compareTo(this.StockCode) != 0) return;
					if(spr.getMarket().compareTo(this.Market) != 0) return;
					
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
			} else if(ObserableUnlockTrade.class.isInstance(o))
			{
				ObserableUnlockTrade obserable = (ObserableUnlockTrade)o;
				synchronized (obserable)
				{
					UnlockTradeResult utr = obserable.getUnlockTradeResult();

					onUnlockTradeResult(utr);
				}
			}
		}
	}
}
