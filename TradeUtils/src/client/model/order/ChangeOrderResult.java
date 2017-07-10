package client.model.order;

import client.model.base.BaseAccountResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 修改订单返回信息
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 * 
 * 注：OrderID、LocalID只用设一个非0有效值(因PlaceOrder只能返回LocalID), OrderID参数优先处理
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ChangeOrderResult extends BaseAccountResult {
	private String LocalID;			//PlaceOrder 后给出的本地 ID, 推测是 FUTU 在本地客户端上的识别 ID
	private String OrderID;			//交易 ID 注：OrderID、LocalID只用设一个非0有效值(因PlaceOrder只能返回LocalID), OrderID参数优先处理
	private String SvrResult;		//Svr的返回结果
	

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

	@JsonProperty("SvrResult")
	public String getSvrResult() {
		return SvrResult;
	}

	@JsonProperty("SvrResult")
	public void setSvrResult(String svrResult) {
		SvrResult = svrResult;
	}
	
}
