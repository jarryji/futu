package strategy;

import com.google.gson.Gson;

import client.client.FutuClient;
import client.model.account.HKOrderItem;
import client.model.account.QueryAccountInfoResult;
import client.model.account.QueryOrderListResult;
import client.model.account.QueryStockListResult;
import client.model.account.UnlockTradeResult;
import client.model.base.BasePacket;
import client.model.order.ChangeOrderResult;
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusParam;
import client.model.order.SetOrderStatusResult;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceResult;


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

public class TestStrategy extends BaseStrategy {

	@Override
	protected void onGearPriceResult(GearPriceResult gpr) {}
	@Override
	protected void onStockPriceResult(StockPriceResult spr) {}
	
	@Override
	protected void onPlaceOrderResult(PlaceOrderResult por) {
//		FutuClient futuClient = FutuClient.getInstance();
//		String cookie = futuClient.getNewCookie();
//		String localId = por.getLocalID();
//		String orderId = "0";
//		String newPrice = 210.200 * 1000 + "";
//		String newQty = "100";
//		futuClient.changeOrder(cookie, BasePacket.MARKET_HK, localId , orderId, newPrice, newQty);
		
		FutuClient futuClient = FutuClient.getInstance();
		String market = BasePacket.MARKET_HK;
		String cookie = futuClient.getNewCookie();
		String localId = por.getLocalID();
		String orderId = "0";
		futuClient.setOrderStatus(cookie, market, localId, orderId, SetOrderStatusParam.ORDER_TYPE_CANCEL);
	}
	@Override
	protected void onChangeOrderResult(ChangeOrderResult cor) {
		FutuClient futuClient = FutuClient.getInstance();
		String market = BasePacket.MARKET_HK;
		String cookie = futuClient.getNewCookie();
		String localId = cor.getLocalID();
		String orderId = cor.getOrderID();
		futuClient.setOrderStatus(cookie, market, localId, orderId, SetOrderStatusParam.ORDER_TYPE_CANCEL);
	}
	@Override
	protected void onSetOrderStatusResult(SetOrderStatusResult sosr) {
		System.out.println(
				"onSetOrderStatusResult cookie: " + sosr.getCookie() +
				"orderId: " + sosr.getOrderID()
				);

		FutuClient futuClient = FutuClient.getInstance();
		String market = BasePacket.MARKET_HK;
		String cookie = futuClient.getNewCookie();
		
		futuClient.getStockList(cookie, market);
		
	}
	@Override
	protected void onAccountInfoResult(QueryAccountInfoResult qair) {

		FutuClient futuClient = FutuClient.getInstance();
		String market = BasePacket.MARKET_HK;
		String cookie = futuClient.getNewCookie();
		
		//futuClient.getAccountInfo(cookie);
	}
	@Override
	protected void onOrderListResult(QueryOrderListResult qosr) {

		FutuClient futuClient = FutuClient.getInstance();
		String market = BasePacket.MARKET_HK;
		String cookie = futuClient.getNewCookie();

		Gson gson = new Gson();
		for(HKOrderItem hkOrderItem : qosr.getHKOrderArr())
		{
			System.out.println(gson.toJson(hkOrderItem));
		}
		
	}
	@Override
	protected void onStockListResult(QueryStockListResult qslr) {

		FutuClient futuClient = FutuClient.getInstance();
		String market = BasePacket.MARKET_HK;
		String cookie = futuClient.getNewCookie();
		
//		futuClient.getOrderList(cookie, market);
	}
	@Override
	protected void onUnlockTradeResult(UnlockTradeResult utr) {
		System.out.println(
				"UnlockTradeResult cookie: " + utr.getCookie() +
				" SvrResult: " + utr.getSvrResult()
				);
	}
	

	 /**
	  * 计算
	  */
	@Override
	protected boolean judge()
	 {
		 return false;
	 }

}
