package test;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import strategy.SimpleStopLoss;
import trade.TradeManager;
import client.client.FutuClient;
import client.client.SimulateData;
import client.model.*;
import client.model.account.*;
import client.model.order.*;
import client.model.quote.*;
import client.model.base.*;
import client.model.base.BasePacket.*;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolTest {
	private static final Logger logger = LoggerFactory.getLogger(ProtocolTest.class);
	private static FutuClient futuClient;
	private TradeManager tradeManager = TradeManager.getInstance();
	private SimpleStopLoss simpleStopLoss = new SimpleStopLoss();
	private String serverAddress = "10.211.55.13";
	private int serverPort = 59988;
	private String market = BasePacket.MARKET_HK;
	private SimulateData simulateData = new SimulateData();
	
	public ProtocolTest () {
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("conf/log4j.properties"));

		simulateData.enabled = false;
		serverAddress = "192.168.1.200";
		serverPort = 59988;
		futuClient = new FutuClient(serverAddress,serverPort);
		futuClient.setClientEnvType(BaseAccountParam.CLIENT_ENV_TYPE_REAL);
		FutuClient.simulateData = simulateData;
		tradeManager.setFutuClient(futuClient);
		simpleStopLoss.setFutuClient(futuClient);

		simpleStopLoss.setMarket(getMarket());
		simpleStopLoss.setStockCode("68799");
		simpleStopLoss.setLossLimit(1);
		simpleStopLoss.start();
		
		futuClient.addObserver(simpleStopLoss);
		futuClient.addObserver(tradeManager);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	};
	
	private String getCookie() {
		return futuClient.getNewCookie();
	}
	
	private String getMarket() {
		return BasePacket.MARKET_HK;
	}
	
	@Test
	public void testStockPrice()
			throws Exception
	{
		futuClient.getStockPrice(BasePacket.MARKET_HK, "00700");
		//futuClient.getGearList(BasePacket.MARKET_HK, "00700");
		
    	Thread.sleep(5000 * 20);
	}
	
	@Test
	public void testAccount()
			throws Exception
	{
		simpleStopLoss.setLossLimit(1);

		//futuClient.getGearList(getMarket(), "68799");
		//futuClient.getStockPrice(getMarket(), "68799");
		futuClient.getAccountInfo(getCookie());
		futuClient.getStockList(getCookie(), market);
		futuClient.getOrderList(getCookie(), market);

    	Thread.sleep(6000 * 100);
	}
	
	@Test
	public void testOrderList()
			throws Exception
	{
		futuClient.getOrderList(getCookie(), getMarket());
    	Thread.sleep(1000 * 20);
	}
	
	@Test
	public void testPlaceOrder()
			throws Exception
	{
		//futuClient.getAccountInfo(getCookie());
		//futuClient.getStockPrice(BasePacket.MARKET_HK, "00700");
		
//		if(!FutuClient.getUnlockedTrade())
//		{
//			futuClient.unlockTrade(getCookie());
//		}
//		Integer price = 213000;
//		
//		for(int i=0;i<200;i++)
//		{
//			if(price < 210000)
//				price = 213000;
//			else
//				price = Integer.valueOf(price - 200);
//			
//			futuClient.placeOrder(getCookie(), getMarket(), PlaceOrderParam.ORDER_SIDE_BUY, "00700", price.toString(), "100");
//			Thread.sleep(1000 * 5);
//		}
	}
	
	
	
	@Test
	public void testChangeOrder()
			throws Exception
	{
		//futuClient.getAccountInfo(getCookie());
		//futuClient.getStockPrice(BasePacket.MARKET_HK, "00700");
		//futuClient.unlockTrade(getCookie());
		//futuClient.changeOrder(getCookie(), getMarket(), "", "", "", "");
		//futuClient.placeOrder(getCookie(), getMarket(), PlaceOrderParam.ORDER_SIDE_BUY, "00700", "213200", "100");

    	Thread.sleep(5000);
	}
	
}
