package client.client;

import com.google.gson.Gson;

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
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusResult;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceResult;

public class SimulateData {
	public QueryOrderListResult qolr = null;
	public QueryStockListResult qslr = null;
	public QueryAccountInfoResult qair = null;
	
	public SetOrderStatusResult sosr = null;
	public ChangeOrderResult cor = null;
	public PlaceOrderResult por = null;
	public GearPriceResult gpr = null;
	public StockPriceResult spr = null;
	public UnlockTradeResult utr = null;

	public static ObserableGearPrice obserableGearPrice;
	public static ObserableStockPrice obserableStockPrice;

	public static ObserablePlaceOrder obserablePlaceOrder;
	public static ObserableChangeOrder obserableChangeOrder;
	public static ObserableSetOrderStatus obserableSetOrderStatus;
	
	public static ObserableAccountInfo obserableAccountInfo;
	public static ObserableOrderList obserableOrderList;
	public static ObserableStockList obserableStockList;
	
	public static ObserableUnlockTrade obserableUnlockTrade;
	
	public boolean enabled = true;

	private Gson gson = new Gson();
	
	public SimulateData()
	{
		obserableGearPrice = FutuHandler.obserableGearPrice;
		obserableStockPrice = FutuHandler.obserableStockPrice;
		obserablePlaceOrder = FutuHandler.obserablePlaceOrder;
		obserableChangeOrder = FutuHandler.obserableChangeOrder;
		obserableSetOrderStatus = FutuHandler.obserableSetOrderStatus;
		obserableAccountInfo = FutuHandler.obserableAccountInfo;
		obserableOrderList = FutuHandler.obserableOrderList;
		obserableStockList = FutuHandler.obserableStockList;
		
		String stringQslr = "{\"Cookie\":\"123123\",\"EnvType\":\"0\",\"HKPositionArr\":[{\"CanSellQty\":\"1000\",\"CostPrice\":\"3505\",\"CostPriceValid\":\"1\",\"MarketVal\":\"4320000\",\"NominalPrice\":\"4320\",\"PLRatio\":\"23252\",\"PLRatioValid\":\"1\",\"PLVal\":\"815000\",\"PLValValid\":\"1\",\"Qty\":\"1000\",\"StockCode\":\"00410\",\"StockName\":\"ＳＯＨＯ中国\",\"Today_BuyQty\":\"0\",\"Today_BuyVal\":\"0\",\"Today_PLVal\":\"20000\",\"Today_SellQty\":\"0\",\"Today_SellVal\":\"0\"},{\"CanSellQty\":\"10000\",\"CostPrice\":\"60\",\"CostPriceValid\":\"1\",\"MarketVal\":\"550000\",\"NominalPrice\":\"55\",\"PLRatio\":\"-8333\",\"PLRatioValid\":\"1\",\"PLVal\":\"-50000\",\"PLValValid\":\"1\",\"Qty\":\"10000\",\"StockCode\":\"68799\",\"StockName\":\"恒指摩通七十牛Q.C\",\"Today_BuyQty\":\"10000\",\"Today_BuyVal\":\"600000\",\"Today_PLVal\":\"-50000\",\"Today_SellQty\":\"0\",\"Today_SellVal\":\"0\"}]}";;
		String stringQolr = "{\"Cookie\":\"123123\",\"EnvType\":\"0\",\"HKOrderArr\":[{\"DealtAvgPrice\":\"60\",\"DealtQty\":\"10000\",\"ErrCode\":\"0\",\"LocalID\":\"1475544790896\",\"OrderID\":\"20973880\",\"OrderSide\":\"0\",\"OrderType\":\"0\",\"Price\":\"60\",\"Qty\":\"10000\",\"Status\":\"3\",\"StockCode\":\"68799\",\"StockName\":\"恒指摩通七十牛Q.C\",\"SubmitedTime\":\"1475544790\",\"UpdatedTime\":\"1475544813\"},{\"DealtAvgPrice\":\"0\",\"DealtQty\":\"0\",\"ErrCode\":\"0\",\"LocalID\":\"1475548407224\",\"OrderID\":\"20980354\",\"OrderSide\":\"1\",\"OrderType\":\"0\",\"Price\":\"65\",\"Qty\":\"10000\",\"Status\":\"6\",\"StockCode\":\"68799\",\"StockName\":\"恒指摩通七十牛Q.C\",\"SubmitedTime\":\"1475548407\",\"UpdatedTime\":\"1475548416\"},{\"DealtAvgPrice\":\"0\",\"DealtQty\":\"0\",\"ErrCode\":\"0\",\"LocalID\":\"1475548428680\",\"OrderID\":\"20980372\",\"OrderSide\":\"1\",\"OrderType\":\"0\",\"Price\":\"65\",\"Qty\":\"10000\",\"Status\":\"4\",\"StockCode\":\"68799\",\"StockName\":\"恒指摩通七十牛Q.C\",\"SubmitedTime\":\"1475548428\",\"UpdatedTime\":\"1475548433\"},{\"DealtAvgPrice\":\"0\",\"DealtQty\":\"0\",\"ErrCode\":\"0\",\"LocalID\":\"1475548436334\",\"OrderID\":\"20980380\",\"OrderSide\":\"1\",\"OrderType\":\"0\",\"Price\":\"65\",\"Qty\":\"10000\",\"Status\":\"6\",\"StockCode\":\"68799\",\"StockName\":\"恒指摩通七十牛Q.C\",\"SubmitedTime\":\"1475548436\",\"UpdatedTime\":\"1475548453\"},{\"DealtAvgPrice\":\"0\",\"DealtQty\":\"0\",\"ErrCode\":\"0\",\"LocalID\":\"1475548456310\",\"OrderID\":\"20980408\",\"OrderSide\":\"1\",\"OrderType\":\"0\",\"Price\":\"65\",\"Qty\":\"10000\",\"Status\":\"1\",\"StockCode\":\"68799\",\"StockName\":\"恒指摩通七十牛Q.C\",\"SubmitedTime\":\"1475548456\",\"UpdatedTime\":\"1475548456\"}]}";
		String stringQair = "{\"Cookie\":\"1\",\"DJZJ\":\"0\",\"EnvType\":\"0\",\"GPBZJ\":\"0\",\"KQXJ\":\"101727850\",\"Power\":\"101727850\",\"XJJY\":\"101727850\",\"YYJDE\":\"0\",\"ZCJZ\":\"106027850\",\"ZGJDE\":\"0\",\"ZQSZ\":\"4300000\",\"ZSJE\":\"0\"}";
		String stringSpr	= "{\"Close\":\"60\",\"Cur\":\"60\",\"High\":\"70\",\"LastClose\":\"29\",\"Low\":\"53\",\"Market\":\"1\",\"Open\":\"60\",\"StockCode\":\"68799\",\"Time\":\"57600\",\"Turnover\":\"15550410000\",\"Vol\":\"249680000\"}";
		String stringGpr = "{\"GearArr\":[{\"BuyOrder\":\"4\",\"BuyPrice\":\"59\",\"BuyVol\":\"6280000\",\"SellOrder\":\"6\",\"SellPrice\":\"60\",\"SellVol\":\"4400000\"},{\"BuyOrder\":\"6\",\"BuyPrice\":\"58\",\"BuyVol\":\"7990000\",\"SellOrder\":\"8\",\"SellPrice\":\"61\",\"SellVol\":\"7760000\"},{\"BuyOrder\":\"3\",\"BuyPrice\":\"57\",\"BuyVol\":\"3430000\",\"SellOrder\":\"8\",\"SellPrice\":\"62\",\"SellVol\":\"2900000\"},{\"BuyOrder\":\"3\",\"BuyPrice\":\"56\",\"BuyVol\":\"2470000\",\"SellOrder\":\"4\",\"SellPrice\":\"63\",\"SellVol\":\"1680000\"},{\"BuyOrder\":\"3\",\"BuyPrice\":\"55\",\"BuyVol\":\"270000\",\"SellOrder\":\"5\",\"SellPrice\":\"64\",\"SellVol\":\"3320000\"}],\"Market\":\"1\",\"StockCode\":\"68799\"}";
		
		qolr = gson.fromJson(stringQolr, QueryOrderListResult.class);
		qslr = gson.fromJson(stringQslr, QueryStockListResult.class);
		qair = gson.fromJson(stringQair, QueryAccountInfoResult.class);
		spr = gson.fromJson(stringSpr, StockPriceResult.class);
		gpr = gson.fromJson(stringGpr, GearPriceResult.class);
		
	}
	
	public void onStockList() {
	    if(qslr == null || enabled == false) return;
	    obserableStockList.setQueryStockListResult(qslr);
	    obserableStockList.onUpdate();
	}

	public void onOrderList() {
	    if(qolr == null || enabled == false) return;
	    obserableOrderList.setQueryOrderListResult(qolr);
	    obserableOrderList.onUpdate();
	}

	public void onAccountInfo() {
	    if(qair == null || enabled == false) return;
	    obserableAccountInfo.setQueryAccountInfoResult(qair);
	    obserableAccountInfo.onUpdate();
	}

	public void onSetOrderStatus() {
	    if(sosr == null || enabled == false) return;
	    obserableSetOrderStatus.setSetOrderStatusResult(sosr);
	    obserableSetOrderStatus.onUpdate();
	}

	public void onChangeOrder() {
	    if(cor == null || enabled == false) return;
	    obserableChangeOrder.setChangeOrderResult(cor);
	    obserableChangeOrder.onUpdate();
	}

	public void onPlaceOrder() {
	    if(por == null || enabled == false) return;
	    obserablePlaceOrder.setPlaceOrderResult(por);
	    obserablePlaceOrder.onUpdate();
	}
	
	public void onGearPrice() {
	    if(gpr == null || enabled == false) return;
	    obserableGearPrice.setGearPriceResult(gpr);
	    obserableGearPrice.onUpdate();
	}
	
	public void onStockPrice() {
	    if(spr == null || enabled == false) return;
	    obserableStockPrice.setStockPriceResult(gson.fromJson(gson.toJson(spr),StockPriceResult.class));
	    obserableStockPrice.onUpdate();
	}

	public void onUnloadTrade() {
	    if(spr == null || enabled == false) return;
	    obserableUnlockTrade.setUnlockTradeResult(utr);
	    obserableUnlockTrade.onUpdate();	    
	}
}
