package client.model.account;

import client.model.base.BaseAccountParam;
/**
 * 查询执仓证券列表信息参数
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
public class QueryStockListParam extends BaseAccountParam {
	public QueryStockListParam(String cookie, String envType)
	{
		super(cookie, envType);
	}
}
