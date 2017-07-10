package trade;

import client.model.base.BaseObserable;

public class ObserableTradeOrder  extends BaseObserable {
	private String tradeMessage = "";
	private String shortMessage = "";
	private String stockCode = "";
	
	public String getTradeMessage() {
		return tradeMessage;
	}

	public void setTradeMessage(String tradeMessage) {
		this.tradeMessage = tradeMessage;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	
}
