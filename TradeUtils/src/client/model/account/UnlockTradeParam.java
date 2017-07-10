package client.model.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import client.model.base.BaseAccountParam;
/**
 * 解锁交易参数
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class UnlockTradeParam extends BaseAccountParam {
	public UnlockTradeParam(String cookie, String envType)
	{
		super(cookie, envType);
	}
	
	public String Password;

	@JsonProperty("Password")
	public String getPassword() {
		return Password;
	}

	@JsonProperty("Password")
	public void setPassword(String password) {
		this.Password = password;
	}
	
}
