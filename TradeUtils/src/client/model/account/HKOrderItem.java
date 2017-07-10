package client.model.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 订单信息
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class HKOrderItem {
	public final static int ORDER_STATUS_WAIT_PROCESS			=	0;	//服务器处理中...
	public final static int ORDER_STATUS_WAIT_DEAL				=	1;	//等待成交
	public final static int ORDER_STATUS_PARTIAL_DEAL			=	2;	//部分成交
	public final static int ORDER_STATUS_ALL_DEAL				=	3;	//全部成交
	public final static int ORDER_STATUS_ORDER_DISABLED			=	4;	//已失效
	public final static int ORDER_STATUS_ORDER_FAILED			=	5;	//下单失败 
	public final static int ORDER_STATUS_ORDER_CANCELED			=	6;	//已撤单
	public final static int ORDER_STATUS_ORDER_DELETED			=	7;	//已删除
	public final static int ORDER_STATUS_ORDER_WAIT_OPEN		=	8;	//等待开盘
	public final static int ORDER_STATUS_LOCAL_SENT				=	21;	//本地已发送
	public final static int ORDER_STATUS_LOCAL_SENT_FAILED		=	22;	//本地已发送，服务器返回下单失败，没产生订单
	public final static int ORDER_STATUS_LOCAL_SENT_WAIT_RET	=	23;	//本地已发送，等待服务器返回超时 
	
	private String DealtAvgPrice;	//成交均价
	private String DealtQty;		//成交数量
	private String ErrCode;			//
	private String LocalID;			//订单的本地标识
	private String OrderID;			//定单id   
	private String Price;			//订单价格   
	private String Qty;				//订单数量
	private String OrderSide;		//交易方向 (0: 买入 1: 卖出)
	private String Status;			//订单状态 
	private String StockCode;		//股票代码 
	private String StockName;		//股票名称
	private String SubmitedTime;	//服务器收到的订单提交时间(GMT)
	private String OrderType;		//交易类型
	private String UpdatedTime;		//订单最后更新的时间(GMT)

	@JsonProperty("DealtAvgPrice")
	public String getDealtAvgPrice() {
		return DealtAvgPrice;
	}

	@JsonProperty("DealtAvgPrice")
	public void setDealtAvgPrice(String dealtAvgPrice) {
		DealtAvgPrice = dealtAvgPrice;
	}

	@JsonProperty("DealtQty")
	public String getDealtQty() {
		return DealtQty;
	}

	@JsonProperty("DealtQty")
	public void setDealtQty(String dealtQty) {
		DealtQty = dealtQty;
	}

	@JsonProperty("ErrCode")
	public String getErrCode() {
		return ErrCode;
	}

	@JsonProperty("ErrCode")
	public void setErrCode(String errCode) {
		ErrCode = errCode;
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

	@JsonProperty("OrderSide")
	public String getOrderSide() {
		return OrderSide;
	}

	@JsonProperty("OrderSide")
	public void setOrderSide(String orderSide) {
		OrderSide = orderSide;
	}

	@JsonProperty("Status")
	public String getStatus() {
		return Status;
	}

	@JsonProperty("Status")
	public void setStatus(String status) {
		Status = status;
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

	@JsonProperty("SubmitedTime")
	public String getSubmitedTime() {
		return SubmitedTime;
	}

	@JsonProperty("SubmitedTime")
	public void setSubmitedTime(String submitedTime) {
		SubmitedTime = submitedTime;
	}

	@JsonProperty("OrderType")
	public String getOrderType() {
		return OrderType;
	}

	@JsonProperty("OrderType")
	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	@JsonProperty("UpdatedTime")
	public String getUpdatedTime() {
		return UpdatedTime;
	}

	@JsonProperty("UpdatedTime")
	public void setUpdatedTime(String updatedTime) {
		UpdatedTime = updatedTime;
	}
}