package client.model.order;

import client.model.base.BaseAccountResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 下定单返回信息
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 * 
 * 注：OrderID、LocalID只用设一个非0有效值(因PlaceOrder只能返回LocalID), OrderID参数优先处理
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class PlaceOrderResult extends BaseAccountResult {
	private String LocalID;			//交易方向 0: 买入  1: 卖出
	private String SvrResult;		//交易类型

	@JsonProperty("LocalID")
	public String getLocalID() {
		return LocalID;
	}
	
	@JsonProperty("LocalID")
	public void setLocalID(String localID) {
		LocalID = localID;
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
