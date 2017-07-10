package client.model.quote;

import client.util.JsonUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 取回证券报价返回
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class StockPriceResult {
	private String Close;		//收盘价
	private String High;		//最高价
	private String LastClose;	//昨收
	private String Low;			//最低
	private String Open;		//开盘
	private String Turnover;	//成交额
	private String Vol;			//成交量
	private String Cur;			//现价
	private String Time;		//报价最后更新时间
	private String Market;
	private String StockCode;
	
	@JsonProperty("Close")
	public String getClose() {
		return Close;
	}

	@JsonProperty("Close")
	public void setClose(String close) {
		Close = close;
	}

	@JsonProperty("High")
	public String getHigh() {
		return High;
	}

	@JsonProperty("High")
	public void setHigh(String high) {
		High = high;
	}

	@JsonProperty("LastClose")
	public String getLastClose() {
		return LastClose;
	}

	@JsonProperty("LastClose")
	public void setLastClose(String lastClose) {
		LastClose = lastClose;
	}

	@JsonProperty("Low")
	public String getLow() {
		return Low;
	}

	@JsonProperty("Low")
	public void setLow(String low) {
		Low = low;
	}

	@JsonProperty("Open")
	public String getOpen() {
		return Open;
	}

	@JsonProperty("Open")
	public void setOpen(String open) {
		Open = open;
	}

	@JsonProperty("Turnover")
	public String getTurnover() {
		return Turnover;
	}

	@JsonProperty("Turnover")
	public void setTurnover(String turnover) {
		Turnover = turnover;
	}

	@JsonProperty("Vol")
	public String getVol() {
		return Vol;
	}

	@JsonProperty("Vol")
	public void setVol(String vol) {
		Vol = vol;
	}

	@JsonProperty("Cur")
	public String getCur() {
		return Cur;
	}

	@JsonProperty("Cur")
	public void setCur(String cur) {
		Cur = cur;
	}

	@JsonProperty("Time")
	public String getTime() {
		return Time;
	}

	@JsonProperty("Time")
	public void setTime(String time) {
		Time = time;
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
