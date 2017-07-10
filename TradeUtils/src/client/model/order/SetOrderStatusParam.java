package client.model.order;

import client.model.base.BaseAccountParam;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 设置订单参数信息
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 * 
 * 注：OrderID、LocalID只用设一个非0有效值(因PlaceOrder只能返回LocalID), OrderID参数优先处理
 * SetOrderStatus
 * 0:  撤单
 * 1:  失效
   2:  生效
   3:  删除
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class SetOrderStatusParam extends BaseAccountParam {
	
	public final static String ORDER_TYPE_CANCEL	=	"0";	//撤单
	public final static String ORDER_TYPE_DISABLE	=	"1";	//失效
	public final static String ORDER_TYPE_ENABLE	=	"2";	//生效
	public final static String ORDER_TYPE_DELETE	=	"3";	//删除
	
	private String LocalID;			//PlaceOrder 后给出的本地 ID, 推测是 FUTU 在本地客户端上的识别 ID
	private String OrderID;			//交易 ID
	private String SetOrderStatus;	//更改状态的状态

	public SetOrderStatusParam(String cookie, String envType) {
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

	@JsonProperty("Protocol")
	public String getOrderID() {
		return OrderID;
	}

	@JsonProperty("Protocol")
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	@JsonProperty("Protocol")
	public String getSetOrderStatus() {
		return SetOrderStatus;
	}

	@JsonProperty("Protocol")
	public void setSetOrderStatus(String setOrderStatus) {
		SetOrderStatus = setOrderStatus;
	}
	
	
}
