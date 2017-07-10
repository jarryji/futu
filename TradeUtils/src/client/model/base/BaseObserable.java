package client.model.base;

import java.util.Observable;
/**
 * 被观察者基类
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
public class BaseObserable extends Observable {

	public void onUpdate()
	{
		setChanged();
		notifyObservers();
	}
}
