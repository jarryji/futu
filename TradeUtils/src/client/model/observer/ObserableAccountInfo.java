package client.model.observer;


import client.model.account.QueryAccountInfoResult;
import client.model.base.BaseObserable;
/**
 * 账号信息 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserableAccountInfo extends BaseObserable {
	private QueryAccountInfoResult queryAccountInfoResult;

	public QueryAccountInfoResult getQueryAccountInfoResult() {
		return queryAccountInfoResult;
	}

	public void setQueryAccountInfoResult(
			QueryAccountInfoResult queryAccountInfoResult) {
		this.queryAccountInfoResult = queryAccountInfoResult;
	}

}

