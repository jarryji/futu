package client.model.observer;

import client.model.base.BaseObserable;
import client.model.quote.StockPriceResult;

/**
 * 股票报价 被观察者
 * @author jarry
 *
 */
public class ObserableStockPrice extends BaseObserable {
	private StockPriceResult stockPriceResult;
	
	public StockPriceResult getStockPriceResult() {
		return stockPriceResult;
	}

	public void setStockPriceResult(StockPriceResult stockPriceResult) {
		this.stockPriceResult = stockPriceResult;
	}
}
