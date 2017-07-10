package client.model.quote;

import client.util.JsonUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 取回证券摆盘数据信息参数
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class GearPriceParam {
	private String GetGearNum;		//摆盘数据结点
	private String Market;			//交易市场
	private String StockCode;		//股票代码
	public static String DEFAULT_GEAR_NUMBER	=	"5";
	
	public GearPriceParam(String getGearNum, String market,String stockCode)
	{
		GetGearNum = getGearNum;
		Market = market;
		StockCode = stockCode;
	}
	
	@JsonProperty("GetGearNum")
	public String getGetGearNum() {
		return GetGearNum;
	}

	@JsonProperty("GetGearNum")
	public void setGetGearNum(String getGearNum) {
		GetGearNum = getGearNum;
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
