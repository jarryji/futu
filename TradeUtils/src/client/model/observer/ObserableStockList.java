package client.model.observer;

import client.model.account.QueryStockListResult;
import client.model.base.BaseObserable;
/**
 * 执仓列表 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserableStockList extends BaseObserable {
	private QueryStockListResult queryStockListResult;

	public QueryStockListResult getQueryStockListResult() {
		return queryStockListResult;
	}

	public void setQueryStockListResult(QueryStockListResult queryStockListResult) {
		this.queryStockListResult = queryStockListResult;
	}

}

