package client.model.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 基本返回
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class BaseAccountResult {
	public final static String CLIENT_ENV_TYPE_REAL		=	"0";	//真实交易 
	public final static String CLIENT_ENV_TYPE_SIMULATE	=	"1";	//仿真交易
	
	private String Cookie;			//操作标识
	private String EnvType;			//交易环境参数 0=真实交易  1=仿真交易
	
	@JsonProperty("Cookie")
	public String getCookie() {
		return Cookie;
	}

	@JsonProperty("Cookie")
	public void setCookie(String cookie) {
		Cookie = cookie;
	}

	@JsonProperty("EnvType")
	public String getEnvType() {
		return EnvType;
	}

	@JsonProperty("EnvType")
	public void setEnvType(String envType) {
		EnvType = envType;
	}
}
