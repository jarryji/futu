package client.model.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 执仓证券信息
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class HKPositionItem {
	private String CanSellQty;		//可卖数量
	private String CostPrice;		//成本价
	private String CostPriceValid;	//成本价是否有效 (非0表求有效)
	private String MarketVal;		//市值
	private String NominalPrice;	//市价  
	private String PLRatio;			//盈亏比例 (*100000) eg: 1% = 1000
	private String PLRatioValid;	//盈亏比例是否有效 (非0表求有效)
	private String PLVal;			//盈亏金额
	private String PLValValid;		//盈亏金额是否有效 (非0表求有效)
	private String Qty;				//持有数量
	private String StockCode;		//股票代码 
	private String StockName;		//股票名称
	private String Today_BuyQty;	//今日买入成交量
	private String Today_BuyVal;	//今日买入成交额
	private String Today_PLVal;		//今日盈亏金额
	private String Today_SellQty;	//今日卖出成交量
	private String Today_SellVal;	//今日卖出成交额

	@JsonProperty("CanSellQty")
	public String getCanSellQty() {
		return CanSellQty;
	}

	@JsonProperty("CanSellQty")
	public void setCanSellQty(String canSellQty) {
		CanSellQty = canSellQty;
	}

	@JsonProperty("CostPrice")
	public String getCostPrice() {
		return CostPrice;
	}

	@JsonProperty("CostPrice")
	public void setCostPrice(String costPrice) {
		CostPrice = costPrice;
	}

	@JsonProperty("CostPriceValid")
	public String getCostPriceValid() {
		return CostPriceValid;
	}

	@JsonProperty("CostPriceValid")
	public void setCostPriceValid(String costPriceValid) {
		CostPriceValid = costPriceValid;
	}

	@JsonProperty("MarketVal")
	public String getMarketVal() {
		return MarketVal;
	}

	@JsonProperty("MarketVal")
	public void setMarketVal(String marketVal) {
		MarketVal = marketVal;
	}

	@JsonProperty("NominalPrice")
	public String getNominalPrice() {
		return NominalPrice;
	}

	@JsonProperty("NominalPrice")
	public void setNominalPrice(String nominalPrice) {
		NominalPrice = nominalPrice;
	}

	@JsonProperty("PLRatio")
	public String getPLRatio() {
		return PLRatio;
	}

	@JsonProperty("PLRatio")
	public void setPLRatio(String pLRatio) {
		PLRatio = pLRatio;
	}

	@JsonProperty("PLRatioValid")
	public String getPLRatioValid() {
		return PLRatioValid;
	}

	@JsonProperty("PLRatioValid")
	public void setPLRatioValid(String pLRatioValid) {
		PLRatioValid = pLRatioValid;
	}

	@JsonProperty("PLVal")
	public String getPLVal() {
		return PLVal;
	}

	@JsonProperty("PLVal")
	public void setPLVal(String pLVal) {
		PLVal = pLVal;
	}

	@JsonProperty("PLValValid")
	public String getPLValValid() {
		return PLValValid;
	}

	@JsonProperty("PLValValid")
	public void setPLValValid(String pLValValid) {
		PLValValid = pLValValid;
	}

	@JsonProperty("Qty")
	public String getQty() {
		return Qty;
	}

	@JsonProperty("Qty")
	public void setQty(String qty) {
		Qty = qty;
	}

	@JsonProperty("StockCode")
	public String getStockCode() {
		return StockCode;
	}

	@JsonProperty("StockCode")
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}

	@JsonProperty("StockName")
	public String getStockName() {
		return StockName;
	}

	@JsonProperty("StockName")
	public void setStockName(String stockName) {
		StockName = stockName;
	}

	@JsonProperty("Today_BuyQty")
	public String getToday_BuyQty() {
		return Today_BuyQty;
	}

	@JsonProperty("Today_BuyQty")
	public void setToday_BuyQty(String today_BuyQty) {
		Today_BuyQty = today_BuyQty;
	}

	@JsonProperty("Today_BuyVal")
	public String getToday_BuyVal() {
		return Today_BuyVal;
	}

	@JsonProperty("Today_BuyVal")
	public void setToday_BuyVal(String today_BuyVal) {
		Today_BuyVal = today_BuyVal;
	}

	@JsonProperty("Today_PLVal")
	public String getToday_PLVal() {
		return Today_PLVal;
	}

	@JsonProperty("Today_PLVal")
	public void setToday_PLVal(String today_PLVal) {
		Today_PLVal = today_PLVal;
	}

	@JsonProperty("Today_SellQty")
	public String getToday_SellQty() {
		return Today_SellQty;
	}

	@JsonProperty("Today_SellQty")
	public void setToday_SellQty(String today_SellQty) {
		Today_SellQty = today_SellQty;
	}

	@JsonProperty("Today_SellVal")
	public String getToday_SellVal() {
		return Today_SellVal;
	}

	@JsonProperty("Today_SellVal")
	public void setToday_SellVal(String today_SellVal) {
		Today_SellVal = today_SellVal;
	}
}