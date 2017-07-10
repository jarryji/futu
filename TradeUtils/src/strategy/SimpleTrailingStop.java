package strategy;

import client.model.account.QueryAccountInfoResult;
import client.model.account.QueryOrderListResult;
import client.model.account.QueryStockListResult;
import client.model.account.UnlockTradeResult;
import client.model.order.ChangeOrderResult;
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusResult;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceResult;

/**
 * 简单跟踪止损法
 * 
 * @author jarry
 * 
 */
public class SimpleTrailingStop extends BaseStrategy {
	@Override
	protected void onGearPriceResult(GearPriceResult gpr) {}
	@Override
	protected void onStockPriceResult(StockPriceResult spr) {}
	@Override
	protected void onPlaceOrderResult(PlaceOrderResult por) {}
	@Override
	protected void onChangeOrderResult(ChangeOrderResult cor) {}
	@Override
	protected void onSetOrderStatusResult(SetOrderStatusResult sosr) {}
	@Override
	protected void onAccountInfoResult(QueryAccountInfoResult qair) {}
	@Override
	protected void onOrderListResult(QueryOrderListResult qosr) {}
	@Override
	protected void onStockListResult(QueryStockListResult qslr) {}
	@Override
	protected void onUnlockTradeResult(UnlockTradeResult utr) {}

	@Override
	protected boolean judge() {
		return false;
	}
}
