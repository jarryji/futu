package client.model.order;

import client.model.base.BaseAccountParam;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 下定单参数信息
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 * 
 * 注：OrderID、LocalID只用设一个非0有效值(因PlaceOrder只能返回LocalID), OrderID参数优先处理
 * 
 * OrderSide
 * 0: 买入
 * 1: 卖出
 * 
 * OrderType
 * 0： 增强限价单(普通交易)
 * 1： 竞价单(竞价交易)
 * 2：限价单 （暂不支持) 
 * 3： 竞价限价单(竞价限价)
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceOrderParam extends BaseAccountParam {
	public final static String ORDER_TYPE_ENHANCED_LIMIT	= "0";	//增强限价单(普通交易)
	public final static String ORDER_TYPE_AUCTION			= "1";	//竞价单(竞价交易)
	public final static String ORDER_TYPE_LIMITED_PRICE		= "2";	//限价单(暂不支持) 
	public final static String ORDER_TYPE_AUCTION_LIMIT		= "3";	//竞价限价单(竞价限价)

	public final static String ORDER_SIDE_BUY				= "0";	//买入
	public final static String ORDER_SIDE_SELL				= "1";	//卖出
	
	private String OrderSide;		//交易方向 0: 买入  1: 卖出
	private String OrderType;		//交易类型 
	private String Price;			//交易价格	以厘为单位 元 * 1000 的整型
	private String Qty;				//交易数量
	private String StockCode;		//股票代码
	
	public PlaceOrderParam(String cookie, String envType) {
		super(cookie, envType);
	}
	
	@JsonProperty("OrderSide")
	public String getOrderSide() {
		return OrderSide;
	}
	
	@JsonProperty("OrderSide")
	public void setOrderSide(String orderSide) {
		OrderSide = orderSide;
	}
	
	@JsonProperty("OrderType")
	public String getOrderType() {
		return OrderType;
	}
	
	@JsonProperty("OrderType")
	public void setOrderType(String orderType) {
		OrderType = orderType;
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
	
	@JsonProperty("StockCode")
	public String getStockCode() {
		return StockCode;
	}
	
	@JsonProperty("StockCode")
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}
}
