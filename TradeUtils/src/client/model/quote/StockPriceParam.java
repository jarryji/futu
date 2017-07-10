package client.model.quote;

import client.util.JsonUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 取回证券报价参数
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class StockPriceParam {
	private String Market;			//交易市场
	private String StockCode;		//股票代码
	
	public StockPriceParam(String market,String stockCode)
	{
		Market = market;
		StockCode = stockCode;
	}
	
	@JsonProperty("Market")
	public String getMarket() {
		return Market;
	}

	@JsonProperty("Market")
	public void setMarket(String market) {
		Market = market;
	}

	@JsonProperty("StockCode")
	public String getStockCode() {
		return StockCode;
	}

	@JsonProperty("StockCode")
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}
	
	@JsonIgnore
	public String toString()
	{
		return JsonUtils.serialize(this);
	}
}
