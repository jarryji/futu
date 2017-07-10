package client.model.account;
/**
 * 解锁交易参数返回
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
public class UnlockTradeResult {
	private String Cookie;			//操作标识
	private String SvrResult;		//Svr的返回结果
	public String getCookie() {
		return Cookie;
	}
	public void setCookie(String cookie) {
		Cookie = cookie;
	}
	public String getSvrResult() {
		return SvrResult;
	}
	public void setSvrResult(String svrResult) {
		SvrResult = svrResult;
	}
}
