package client.model.observer;

import client.model.account.QueryOrderListResult;
import client.model.base.BaseObserable;
/**
 * 定单列表 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserableOrderList extends BaseObserable {
	private QueryOrderListResult queryOrderListResult;

	public QueryOrderListResult getQueryOrderListResult() {
		return queryOrderListResult;
	}

	public void setQueryOrderListResult(QueryOrderListResult queryOrderListResult) {
		this.queryOrderListResult = queryOrderListResult;
	}
}

