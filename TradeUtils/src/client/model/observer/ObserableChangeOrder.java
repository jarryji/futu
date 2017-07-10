package client.model.observer;

import client.model.base.BaseObserable;
import client.model.order.ChangeOrderResult;
/**
 * 修改定单 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserableChangeOrder extends BaseObserable {
	private ChangeOrderResult changeOrderResult;
	
	public ChangeOrderResult getChangeOrderResult() {
		return changeOrderResult;
	}

	public void setChangeOrderResult(ChangeOrderResult changeOrderResult) {
		this.changeOrderResult = changeOrderResult;
	}
}

