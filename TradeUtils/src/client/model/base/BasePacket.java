package client.model.base;
import client.util.JsonUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 数据包基类
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class BasePacket {
	private String Protocol;
	private String Version;
	private String ErrCode;
	private String ErrDesc;
	private String StockCode;
	protected Object ReqParam;
	protected String RetData;

	public final static String CLIENT_AUTHORIZE =	"1000"; //客户端授权
	public final static String STOCK_PRICE 		=	"1001"; //"获取基础报价"
	public final static String GEAR_LIST 		=	"1002"; //"获取摆盘数据"
	public final static String HK_PLACE_ORDER 	=	"6003"; //"港股下单交易"
	public final static String HK_SET_ORDER 	=	"6004"; //"港股设置订单状态"
	public final static String HK_CHANGE_ORDER =	"6005"; //"港股修改订单"
	public final static String HK_UNLOCK_TRADE =	"6006"; //"解锁交易"
	public final static String HK_QUERY_ACCOUNT =	"6007"; //"港股查询帐户信息"
	public final static String HK_QUERY_ORDERS =	"6008"; //"港股查询订单列表"
	public final static String HK_QUERY_STOCKS =	"6009"; //"港股查询持仓列表"
	public final static String UNKNOW 			=	"0000"; //""

	public final static String MARKET_HK			= "1";
	public final static String MARKET_US			= "2";
	public final static String MARKET_SH			= "3";
	public final static String MARKET_SZ			= "4";
	public final static String MARKET_OLD_FUTURE	= "5";
	public final static String MARKET_NEW_FUTURE	= "6";

	public BasePacket()
	{
		Protocol = Protocols.UNKNOW.getprotocolCode();
	}
	
	public BasePacket(Protocols protocol)
	{
		Version = "1";
		Protocol = protocol.getprotocolCode();
	}
	
	@JsonProperty("Protocol")
	public String getProtocol() {
		return Protocol;
	}
	
	@JsonProperty("Protocol")
	public void setProtocol(String protocol) {
		Protocol = protocol;
	}
	
	@JsonProperty("Version")
	public String getVersion() {
		return Version;
	}

	@JsonProperty("Version")
	public void setVersion(String version) {
		Version = version;
	}

	@JsonProperty("ErrCode")
	public String getErrCode() {
		return ErrCode;
	}

	@JsonProperty("ErrCode")
	public void setErrCode(String errCode) {
		ErrCode = errCode;
	}

	@JsonProperty("ErrDesc")
	public String getErrDesc() {
		return ErrDesc;
	}

	@JsonProperty("ErrDesc")
	public void setErrDesc(String errDesc) {
		ErrDesc = errDesc;
	}

	@JsonProperty("StockCode")
	public String getStockCode() {
		return StockCode;
	}

	@JsonProperty("StockCode")
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}

	@JsonProperty("ReqParam")
	public Object getReqParam() {
		return ReqParam;
	}

	@JsonProperty("ReqParam")
	public void setReqParam(Object reqParam) {
		ReqParam = reqParam;
	}

	@JsonProperty("RetData")
	public String getRetData() {
		return RetData;
	}

	@JsonProperty("RetData")
	public void setRetData(String retData) {
		RetData = retData;
	}

	@JsonIgnore
	public String toString()
	{
		return JsonUtils.serialize(this);
	}
	
	@JsonIgnore
	public static BasePacket fromString(String json)
	{
		//BasePacket bp = new BasePacket(Protocols.UNKNOW);
		
		BasePacket bp = JsonUtils.deserialize(json, BasePacket.class);
		
		return bp;
	}

	public interface Protocol {
	}

	public enum Protocols implements Protocol {
		STOCK_PRICE("1001",		"获取基础报价")
		,GEAR_LIST("1002",		"获取摆盘数据")
		,HK_PLACE_ORDER("6003",	"港股下单交易")
		,HK_SET_ORDER("6004",	"港股设置订单状态")
		,HK_CHANGE_ORDER("6005",	"港股修改订单")
		,UNLOCK_TRADE("6006",		"解锁交易")
		,HK_QUERY_ACCOUNT("6007",	"港股查询帐户信息")
		,HK_QUERY_ORDERS("6008",	"港股查询订单列表")
		,HK_QUERY_STOCKS("6009",	"港股查询持仓列表")
		,UNKNOW("0", "港股查询持仓列表")
		;
		
		private final String protocolCode;
		private final String protocolString;

		Protocols(final String protocolCode, final String protocolString) {
			this.protocolCode = protocolCode;
			this.protocolString = protocolString;
		}

		public String getprotocolCode() {
			return protocolCode;
		}

		public String getprotocolString() {
			return protocolString;
		}
	}
}
