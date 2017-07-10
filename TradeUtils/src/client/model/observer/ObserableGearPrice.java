package client.model.observer;

import client.model.base.BaseObserable;
import client.model.quote.GearPriceResult;
/**
 * 股票摆盘 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserableGearPrice extends BaseObserable {
	private GearPriceResult gearPriceResult;
	
	public GearPriceResult getGearPriceResult() {
		return gearPriceResult;
	}

	public void setGearPriceResult(GearPriceResult gearPriceResult) {
		this.gearPriceResult = gearPriceResult;
	}
	
}
