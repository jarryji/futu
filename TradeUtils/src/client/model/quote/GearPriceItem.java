package client.model.quote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 证券摆盘数据
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class GearPriceItem {
	private String BuyOrder;		//买盘经纪个数
	private String BuyPrice;		//买价
	private String BuyVol;			//买量
	private String SellOrder;		//卖盘经纪个数
	private String SellPrice;		//卖价
	private String SellVol;			//卖量

	@JsonProperty("BuyOrder")
	public String getBuyOrder() {
		return BuyOrder;
	}

	@JsonProperty("BuyOrder")
	public void setBuyOrder(String buyOrder) {
		BuyOrder = buyOrder;
	}

	@JsonProperty("BuyPrice")
	public String getBuyPrice() {
		return BuyPrice;
	}
	
	@JsonProperty("BuyPrice")
	public void setBuyPrice(String buyPrice) {
		BuyPrice = buyPrice;
	}
	
	@JsonProperty("BuyVol")
	public String getBuyVol() {
		return BuyVol;
	}
	
	@JsonProperty("BuyVol")
	public void setBuyVol(String buyVol) {
		BuyVol = buyVol;
	}
	
	@JsonProperty("SellOrder")
	public String getSellOrder() {
		return SellOrder;
	}
	
	@JsonProperty("SellOrder")
	public void setSellOrder(String sellOrder) {
		SellOrder = sellOrder;
	}
	
	@JsonProperty("SellPrice")
	public String getSellPrice() {
		return SellPrice;
	}
	
	@JsonProperty("SellPrice")
	public void setSellPrice(String sellPrice) {
		SellPrice = sellPrice;
	}
	
	@JsonProperty("SellVol")
	public String getSellVol() {
		return SellVol;
	}
	
	@JsonProperty("SellVol")
	public void setSellVol(String sellVol) {
		SellVol = sellVol;
	}
}