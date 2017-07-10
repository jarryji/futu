package client.model.order;

/**
 * 设置订单返回信息
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 * 
 */
public class SetOrderStatusResult {
	private String Cookie;			//操作标识
	private String EnvType;			//交易环境参数 0=真实交易  1=仿真交易
	private String LocalID;			//PlaceOrder 后给出的本地 ID, 推测是 FUTU 在本地客户端上的识别 ID
	private String OrderID;			//交易 ID
	private String SvrResult;		//Svr的返回结果
	public String getCookie() {
		return Cookie;
	}
	public void setCookie(String cookie) {
		Cookie = cookie;
	}
	public String getEnvType() {
		return EnvType;
	}
	public void setEnvType(String envType) {
		EnvType = envType;
	}
	public String getLocalID() {
		return LocalID;
	}
	public void setLocalID(String localID) {
		LocalID = localID;
	}
	public String getOrderID() {
		return OrderID;
	}
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
	public String getSvrResult() {
		return SvrResult;
	}
	public void setSvrResult(String svrResult) {
		SvrResult = svrResult;
	}
	
}
