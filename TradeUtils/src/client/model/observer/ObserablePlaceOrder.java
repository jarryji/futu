package client.model.observer;

import client.model.base.BaseObserable;
import client.model.order.PlaceOrderResult;
/**
 * 下定单 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserablePlaceOrder extends BaseObserable {
	private PlaceOrderResult placeOrderResult;
	
	public PlaceOrderResult getPlaceOrderResult() {
		return placeOrderResult;
	}

	public void setPlaceOrderResult(PlaceOrderResult placeOrderResult) {
		this.placeOrderResult = placeOrderResult;
	}

}

