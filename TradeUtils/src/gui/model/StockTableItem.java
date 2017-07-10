package gui.model;

public class StockTableItem {
	private String stockName;			//名字
	private String stockCode;			//代码
	private int holdVol;				//执仓
	private int curPrice;				//当前价格
	private int costPrice;				//成本价格
	private int lossLimit;				//止损价位
	private Boolean enableAuto;			//开启自动交易(目前只有止损)
	private int openPrice;				//开盘价
	private int lowPrice;				//最低价
	private int highPrice;				//最高价
	private int closePrice;				//上一个交易日收盘价
	private int retirePrice;			//回收价(指数为点)
	private String orderStatus;			//定单状态
	private String updateTime;			//报价更新时间
	
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public int getHoldVol() {
		return holdVol;
	}
	public void setHoldVol(int holdVol) {
		this.holdVol = holdVol;
	}
	public int getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(int curPrice) {
		this.curPrice = curPrice;
	}
	public int getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}
	public int getLossLimit() {
		return lossLimit;
	}
	public void setLossLimit(int lossLimit) {
		this.lossLimit = lossLimit;
	}
	public Boolean isEnableAuto() {
		return enableAuto;
	}
	public void setEnableAuto(Boolean enableAuto) {
		this.enableAuto = enableAuto;
	}
	public int getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(int openPrice) {
		this.openPrice = openPrice;
	}
	public int getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(int closePrice) {
		this.closePrice = closePrice;
	}
	public int getRetirePrice() {
		return retirePrice;
	}
	public void setRetirePrice(int retirePrice) {
		this.retirePrice = retirePrice;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(int lowPrice) {
		this.lowPrice = lowPrice;
	}
	public int getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(int highPrice) {
		this.highPrice = highPrice;
	}
	
	
}
