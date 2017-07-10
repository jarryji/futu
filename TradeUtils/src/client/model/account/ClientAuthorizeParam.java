package client.model.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import client.model.base.BaseAccountParam;

/**
 * 客户端鉴权
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ClientAuthorizeParam extends BaseAccountParam {
	private String userName;
	private String password;
	
	public ClientAuthorizeParam(String cookie, String envType)
	{
		super(cookie, envType);
	}

	@JsonProperty("userName")
	public String getUserName() {
		return userName;
	}

	@JsonProperty("userName")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}
	
}
