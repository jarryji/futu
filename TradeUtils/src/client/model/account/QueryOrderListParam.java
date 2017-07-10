package client.model.account;

import client.model.base.BaseAccountParam;
/**
 * 查询订单列表信息
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
public class QueryOrderListParam extends BaseAccountParam {
	public QueryOrderListParam(String cookie, String envType)
	{
		super(cookie, envType);
	}
}
