package client.model.order;

import client.model.base.BaseAccountParam;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 修改定单参数信息
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 * 
 * 注：OrderID、LocalID只用设一个非0有效值(因PlaceOrder只能返回LocalID), OrderID参数优先处理
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeOrderParam extends BaseAccountParam {
	private String LocalID;			//PlaceOrder 后给出的本地 ID, 推测是 FUTU 在本地客户端上的识别 ID
	private String OrderID;			//交易 ID
	private String Price;			//修改的新价格
	private String Qty;				//修改的新数量
	
	public ChangeOrderParam(String cookie, String envType) {
		super(cookie, envType);
	}
	
	@JsonProperty("LocalID")
	public String getLocalID() {
		return LocalID;
	}

	@JsonProperty("LocalID")
	public void setLocalID(String localID) {
		LocalID = localID;
	}

	@JsonProperty("OrderID")
	public String getOrderID() {
		return OrderID;
	}

	@JsonProperty("OrderID")
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	@JsonProperty("Price")
	public String getPrice() {
		return Price;
	}

	@JsonProperty("Price")
	public void setPrice(String price) {
		Price = price;
	}

	@JsonProperty("Qty")
	public String getQty() {
		return Qty;
	}

	@JsonProperty("Qty")
	public void setQty(String qty) {
		Qty = qty;
	}
}
