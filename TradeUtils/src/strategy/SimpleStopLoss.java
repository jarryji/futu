package strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trade.TradeOrder;
import client.model.account.HKOrderItem;
import client.model.account.HKPositionItem;
import client.model.account.QueryAccountInfoResult;
import client.model.account.QueryOrderListResult;
import client.model.account.QueryStockListResult;
import client.model.account.UnlockTradeResult;
import client.model.order.ChangeOrderResult;
import client.model.order.PlaceOrderParam;
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusResult;


/**
 * 简单止损法.
 * 
 * 以成本价为基准, 以跌落多个个价位时卖出.
 * 用摆盘数据参考(数量,及价格),来匹配最佳卖出价. 
 * 搜索下列数据, 匹配当前执仓, 卖盘 1 少, 买盘 1 也少时, 挂一部份单到 卖 1
 * 
 * ----------------摆盘数据-----------------
 * 买盘					    卖盘
 * 1 0.123    50k( 1)		1 0.124      50k( 2)
 * 2 0.122   100k( 1)		2 0.125		888k( 2)
 * 3 0.121	 150k( 1)		3 0.126		888k( 2)
 * 4 0.120   150k( 1)		4 0.127		888k( 2)
 * 5 0.119	 150k( 1)		5 0.128		888k( 2)
 * 
 * @author jarry
 *
 */

//TODO: 买卖盘数据匹配, 100ms 后未成交,则改价下单.
public class SimpleStopLoss extends BaseStrategy {
	public SimpleStopLoss()
	{
		logger = LoggerFactory.getLogger(SimpleStopLoss.class);
	}
	
	@Override
	protected void onPlaceOrderResult(PlaceOrderResult por) {}
	@Override
	protected void onChangeOrderResult(ChangeOrderResult cor) {}
	@Override
	protected void onSetOrderStatusResult(SetOrderStatusResult sosr) {
		logger.debug("onSetOrderStatusResult cookie: " + sosr.getCookie() + " orderId: " + sosr.getOrderID());
	}
	@Override
	protected void onAccountInfoResult(QueryAccountInfoResult qair) {
	}
	@Override
	protected void onOrderListResult(QueryOrderListResult qolr) {
		if(qolr == null) return;
		//tradeOrder 的量价与上次交易的不同, 可能是其他终端修改了价格.
		if(tradeOrder == null) return;
		//定单已经完成, 重新获取
		if(tradeOrder.getQueueStatus() == TradeOrder.QUEUE_STATUS_ALL_DEAL) ;
		//没有新的订单, 返回
		if((tradeOrder = tradeManager.getValidOrder(StockCode)) == null) return;
		
		if(Integer.parseInt(tradeOrder.getPrice()) != tradePrice || Integer.parseInt(tradeOrder.getQty()) != tradeQty)
		{
			this.notify();
		}
	}
	@Override
	protected void onStockListResult(QueryStockListResult qslr) {
		for(HKPositionItem hkPositionItem : qslr.getHKPositionArr())
		{
			if(hkPositionItem.getStockCode().compareTo(getStockCode()) == 0)
			{
				if(Integer.parseInt(hkPositionItem.getCostPriceValid()) != 0)
					this.costPrice = Integer.parseInt(hkPositionItem.getCostPrice());
				else
					this.costPrice = 0;
				
				this.holdVol = Integer.parseInt(hkPositionItem.getQty());
				this.hkPositionItem = hkPositionItem;

				logger.info("onStockListResult: stock={}, costPrice={}, holdVol={}",getStockCode(), this.costPrice, this.holdVol);
				
				return;
			}
		}
		this.notify();
	}
	@Override
	protected void onUnlockTradeResult(UnlockTradeResult utr) {
		logger.debug("UnlockTradeResult cookie: " + utr.getCookie() + " SvrResult: " + utr.getSvrResult());
	}

/**
 * 
 */
	//TODO: Simple Loss Limit Judge
	@Override
	protected boolean judge()
	 {
		//验证基本数据
		if(hkPositionItem == null || stockPrice == null)
			return false;
		
		if(tradeManager.readyTrade() == false)
			return false;
		
//		if(gearPrice == null || gearPrice.getGearArr() == null)
//			return false;

		//验证证券基本数据
		if(stockPrice.getStockCode().compareTo(getStockCode()) != 0 || stockPrice.getMarket().compareTo(getMarket()) != 0)
			return false;

		//策略是否有效
		if(enabled == false || holdVol <= 0) {
			return false;
		}
		
		//条件是否有效果
		if(lossLimit <= 0) {
			logger.debug("无效止跌参数 lossLimit: " + lossLimit + " <= 0");
			return false;
		}
		
		if(costPrice < 1 || curPrice < 1) return false;

		// 0.220 - 0.219 = 0.001 >= 0.001
		// 触发条件
		if(costPrice - curPrice >= lossLimit)
		{
			sellStock(curPrice, holdVol);
		}

//		protected int costPrice;						//成本 (单位:厘 0.001 * 1000)
//		protected int curPrice;							//现价 (单位:厘 0.001 * 1000)
//		protected int holdVol;							//执有
//		protected int lossLimit;						//跌落触发价(价格子)
//		protected int updateCount = 0;
//		protected boolean enabled = true;
		
//		for(GearPriceItem gearPriceItem : gearPriceResult.getGearArr())
//		{
//		}
		 return false;
	 }
}
