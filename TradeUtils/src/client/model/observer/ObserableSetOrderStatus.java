package client.model.observer;

import client.model.base.BaseObserable;
import client.model.order.SetOrderStatusResult;
/**
 * 设置定单 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserableSetOrderStatus extends BaseObserable {
	private SetOrderStatusResult setOrderStatusResult;
	
	public SetOrderStatusResult getSetOrderStatusResult() {
		return setOrderStatusResult;
	}

	public void setSetOrderStatusResult(SetOrderStatusResult setOrderStatusResult) {
		this.setOrderStatusResult = setOrderStatusResult;
	}

}

