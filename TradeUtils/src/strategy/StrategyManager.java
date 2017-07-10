package strategy;

import java.util.ArrayList;
import java.util.List;

import client.client.FutuClient;

public class StrategyManager {
	private List<BaseStrategy> strategyList = new ArrayList<BaseStrategy>();
	
	public BaseStrategy createStrategy(int strategyType, String market, String stockCode)
	{
		synchronized(strategyList)
		{
			BaseStrategy s;
			if((s = getStragtegy(strategyType, stockCode)) != null) return s;
			
			SimpleStopLoss strategy = new SimpleStopLoss();
			strategy.setFutuClient(FutuClient.getInstance());
			strategy.setEnabled(false);
			strategy.setMarket(market);
			strategy.setStockCode(stockCode);
			strategy.start();
			strategyList.add(strategy);
			FutuClient.getInstance().addObserver(strategy);
			
			return strategy;
		}
	}
	
	public BaseStrategy getStragtegy(int strategyType, String stockCode)
	{
		synchronized(strategyList){
			for(BaseStrategy strategy : strategyList)
			{
				if(strategy.getStockCode().compareTo(stockCode) == 0)
				{
					return strategy;
				}
			}
		}
		return null;
	}
	
}
