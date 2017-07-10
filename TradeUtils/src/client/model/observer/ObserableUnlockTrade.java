package client.model.observer;

import client.model.account.UnlockTradeResult;
import client.model.base.BaseObserable;
/**
 * 解锁交易 被观察者
 * @author jarry
 * @version 1.0
 * @since 2016/9/30
 */
public class ObserableUnlockTrade extends BaseObserable {
	private UnlockTradeResult unlockTradeResult;

	public UnlockTradeResult getUnlockTradeResult() {
		return unlockTradeResult;
	}

	public void setUnlockTradeResult(UnlockTradeResult unlockTradeResult) {
		this.unlockTradeResult = unlockTradeResult;
	}
}

