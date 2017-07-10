package client.model.account;

import client.model.base.BaseAccountParam;
/**
 * 查询账号信息参数
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
public class QueryAccountInfoParam extends BaseAccountParam {
	public QueryAccountInfoParam(String cookie, String envType)
	{
		super(cookie, envType);
	}
}
