package client.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import client.model.PacketCounter;
import client.model.SystemConfig;
import client.model.account.HKOrderItem;
import client.model.account.HKPositionItem;
import client.model.account.QueryAccountInfoParam;
import client.model.account.QueryOrderListParam;
import client.model.account.QueryStockListParam;
import client.model.account.UnlockTradeParam;
import client.model.base.BaseAccountParam;
import client.model.base.BasePacket;
import client.model.base.BasePacket.Protocols;
import client.model.observer.ObserableGearPrice;
import client.model.observer.ObserableStockPrice;
import client.model.order.ChangeOrderParam;
import client.model.order.ChangeOrderResult;
import client.model.order.PlaceOrderParam;
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusParam;
import client.model.quote.GearPriceParam;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceParam;
import client.util.TimeUtils;

/**
 * 
 * @author jarry
 *
 */
public class FutuClient extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(FutuClient.class);
	
	private String clientEnvType = BaseAccountParam.CLIENT_ENV_TYPE_SIMULATE;
	private Integer cookieNumber = 0;
	private static IoConnector connector = new NioSocketConnector();
	private static FutuHandler clientHandler = new FutuHandler();
	private static IoSession session = null;
	
	private static boolean clientAuthorized = false;
	private static boolean unlockedTrade = false;
	private static FutuClient futuClient;
	
	private String serverAddress = "";
	private int serverPort = 59988;
	private String pw = "";
	
	public static SimulateData simulateData = null;
	private PacketCounter packetCounter = FutuHandler.getPacketcounter();

	private static SystemConfig systemConfig = null;
	
	public static SystemConfig getConfig()
	{
		if(FutuClient.systemConfig != null)
			return FutuClient.systemConfig;
		
		SystemConfig config = null;

		try {
			BufferedReader in=new BufferedReader(new FileReader("./config.json"));
			String stringLine;
			StringBuilder sb = new StringBuilder();
			while((stringLine = in.readLine()) != null)
			{
				sb.append(stringLine);
			}
			if(sb.length() > 0)
			{
				String s = sb.toString();
				Gson gson = new Gson();
				config = gson.fromJson(s, SystemConfig.class);
			}
			in.close();
		} catch (Exception e) {
		}
		
		if(config == null)
		{
			config = new SystemConfig();
			config.setPollInterval(1000);
			config.setSyncInterval(1500);
			config.setServerAddress("127.0.0.1");
			config.setServerPort(59988);
		}

		FutuClient.systemConfig = config;
		
		return config;
	}
	
	private void init()
	{
		futuClient = this;
		
		SystemConfig config = getConfig();

		serverAddress = config.getServerAddress();
		serverPort = config.getServerPort();

		this.start();
	}
	
	public static void saveConfig()
	{
		Gson gson = new Gson();
		try {
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter("./config.json"));
			out.write(gson.toJson(FutuClient.systemConfig));
			out.close();
		} catch (IOException e) {
		}
	}
	
	public FutuClient()
	{
		init();
	}
	
	public FutuClient(String address, int port)
	{
		serverAddress = address;
		serverPort = port;
		init();
	}
	
	public String getClientEnvType() {
		return clientEnvType;
	}

	public void setClientEnvType(String clientEnvType) {
		this.clientEnvType = clientEnvType;
	}
	
	public String getNewCookie()
	{
		synchronized(cookieNumber)
		{
			return ++cookieNumber + "";
		}
	}

	@Override
	public void run()
	{
        TextLineCodecFactory lineCodec = new TextLineCodecFactory(Charset.forName("UTF-8"),
        		LineDelimiter.WINDOWS.getValue(),
        		LineDelimiter.WINDOWS.getValue());
        
        lineCodec.setDecoderMaxLineLength(1024*1024);
        lineCodec.setEncoderMaxLineLength(1024*1024);
		connector.setConnectTimeoutMillis(3 * 1000);
		//connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(lineCodec));
		connector.setHandler(clientHandler);
		
		while (!isInterrupted())
		{
			if(null != connector && !connector.isActive())
			{
				try
				{
					synchronized(serverAddress)
					{
						if(serverAddress.length() == 0 || serverPort == 0) continue;
					}

					if(serverAddress.length() > 0 && serverPort > 0)
					{
						ConnectFuture future = connector.connect(new InetSocketAddress(serverAddress, serverPort));
						future.awaitUninterruptibly();
						FutuClient.session = future.getSession();
					}
				}
				catch (Exception e)
				{
				}
			}
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
		if(session != null) session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
	
	/**
	 * 取回实例
	 * @return
	 */
	public static FutuClient getInstance()
	{
		return FutuClient.futuClient;
	}

	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		synchronized(serverAddress) 
		{
			this.serverAddress = serverAddress;
			systemConfig.setServerAddress(serverAddress);
			saveConfig();
		}
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
		systemConfig.setServerPort(serverPort);
		saveConfig();
	}
	public void setClientAuthorized(boolean b) {
		clientAuthorized = b;
	}
	
	public boolean getClientAuthorized() {
		return clientAuthorized;
	}
	
	public static void setUnlockedTrade(boolean b) {
		FutuClient.unlockedTrade = b;
	}
	
	public static boolean getUnlockedTrade()
	{
		return FutuClient.unlockedTrade;
	}
	
	public void addObserver(Observer o)
	{
		synchronized(FutuHandler.obserableGearPrice)
		{
			FutuHandler.obserableGearPrice.addObserver(o);
		}
		synchronized(FutuHandler.obserableStockPrice)
		{
			FutuHandler.obserableStockPrice.addObserver(o);
		}
		synchronized(FutuHandler.obserablePlaceOrder)
		{
			FutuHandler.obserablePlaceOrder.addObserver(o);
		}
		synchronized(FutuHandler.obserableChangeOrder)
		{
			FutuHandler.obserableChangeOrder.addObserver(o);
		}
		synchronized(FutuHandler.obserableSetOrderStatus)
		{
			FutuHandler.obserableSetOrderStatus.addObserver(o);
		}
		synchronized(FutuHandler.obserableAccountInfo)
		{
			FutuHandler.obserableAccountInfo.addObserver(o);
		}
		synchronized(FutuHandler.obserableOrderList)
		{
			FutuHandler.obserableOrderList.addObserver(o);
		}
		synchronized(FutuHandler.obserableStockList)
		{
			FutuHandler.obserableStockList.addObserver(o);
		}
		synchronized(FutuHandler.obserableUnlockTrade)
		{
			FutuHandler.obserableUnlockTrade.addObserver(o);
		}
	}
	
	public void deleteObserver(Observer o)
	{
		synchronized(FutuHandler.obserableGearPrice)
		{
			FutuHandler.obserableGearPrice.deleteObserver(o);
		}
		synchronized(FutuHandler.obserableStockPrice)
		{
			FutuHandler.obserableStockPrice.deleteObserver(o);
		}
		synchronized(FutuHandler.obserablePlaceOrder)
		{
			FutuHandler.obserablePlaceOrder.deleteObserver(o);
		}
		synchronized(FutuHandler.obserableChangeOrder)
		{
			FutuHandler.obserableChangeOrder.deleteObserver(o);
		}
		synchronized(FutuHandler.obserableSetOrderStatus)
		{
			FutuHandler.obserableSetOrderStatus.deleteObserver(o);
		}
		synchronized(FutuHandler.obserableAccountInfo)
		{
			FutuHandler.obserableAccountInfo.deleteObserver(o);
		}
		synchronized(FutuHandler.obserableOrderList)
		{
			FutuHandler.obserableOrderList.deleteObserver(o);
		}
		synchronized(FutuHandler.obserableStockList)
		{
			FutuHandler.obserableStockList.deleteObserver(o);
		}
		synchronized(FutuHandler.obserableUnlockTrade)
		{
			FutuHandler.obserableUnlockTrade.deleteObserver(o);
		}
	}
	
	public static FutuHandler getClientHandler()
	{
		return FutuClient.clientHandler;
	}
	
	public static boolean sendData(String dataString)
	{
		synchronized(FutuClient.getClientHandler())
		{
			if(FutuClient.session != null && connector.isActive())
			{
				//logger.info("Sent message " + dataString);
				session.write(dataString);
			}
		}
		
		return true;
	}
	
	
	//TODO: FutuClient Packet
	/**
	 * 获取摆盘数据, 买卖各5个
	 * @param market
	 * @param stockCode
	 * @return
	 */
	public boolean getGearPrice(String market, String stockCode)
	{
    	packetCounter.addPacketGpp();
    	
		if(isSimutlateEnable())
		if(sendSimulateData(Protocols.GEAR_LIST))
			return true;
	
    	BasePacket bp = new BasePacket(Protocols.GEAR_LIST);
    	bp.setReqParam(new GearPriceParam(GearPriceParam.DEFAULT_GEAR_NUMBER, market,stockCode));
    	return FutuClient.sendData(bp.toString());
	}
	
	/**
	 * 获取报价信息
	 * @param market
	 * @param stockCode
	 * @return
	 */
	public boolean getStockPrice(String market, String stockCode)
	{
    	packetCounter.addPacketSpp();
		if(isSimutlateEnable())
		if(sendSimulateData(Protocols.STOCK_PRICE))
			return true;
		
    	BasePacket bp = new BasePacket(Protocols.STOCK_PRICE);
    	bp.setReqParam(new StockPriceParam(market,stockCode));
    	return FutuClient.sendData(bp.toString());
	}
	
	/**
	 * 获取账号信息
	 * @param cookie	请求识别码
	 * @return
	 */
	public boolean getAccountInfo(String cookie)
	{
    	packetCounter.addPacketQaip();
		if(isSimutlateEnable())
		if(sendSimulateData(Protocols.HK_QUERY_ACCOUNT))
			return true;
		
    	BasePacket bp = new BasePacket(Protocols.HK_QUERY_ACCOUNT);
    	bp.setReqParam(new QueryAccountInfoParam(cookie, getClientEnvType()));
    	return FutuClient.sendData(bp.toString());
	}

	/**
	 * 买卖股票
	 * 
	 * @param cookie	请求识别码
	 * @param market	市场
	 * @param orderSide	PlaceOrderParam.ORDER_SIDE_BUY, PlaceOrderParam.ORDER_SIDE_SELL
	 * @param stockCode	股票代码
	 * @param price		单价
	 * @param qty		数理
	 * @return
	 */
	public boolean placeOrder(String cookie, String market, String orderSide, String stockCode, int price, int qty)
	{
		return placeOrder( cookie,  market,  orderSide,  stockCode,  price + "",  qty + "");
	}
	
	public boolean placeOrder(String cookie, String market, String orderSide, String stockCode, String price, String qty)
	{
    	packetCounter.addPacketPop();
		if(isSimutlateEnable())
		{
			HKOrderItem item = new HKOrderItem();
			item.setDealtAvgPrice("60");
			item.setDealtQty("10000");
			item.setErrCode("0");
			item.setLocalID(cookie);
			item.setOrderID(cookie);
			item.setOrderSide(orderSide);
			item.setOrderType("0");
			item.setPrice(price);
			item.setQty(qty);
			item.setStatus(HKOrderItem.ORDER_STATUS_WAIT_DEAL + "");
			item.setStockCode(stockCode);
			item.setStockName("test");
			item.setSubmitedTime(TimeUtils.getCurrentTimestamp() + "");
			item.setUpdatedTime(TimeUtils.getCurrentTimestamp() + "");
			
			simulateData.qolr.getHKOrderArr().add(item);
			
			simulateData.por = new PlaceOrderResult();
			
			simulateData.por.setCookie(cookie);
			simulateData.por.setLocalID(cookie);
			simulateData.por.setSvrResult("0");
			simulateData.por.setEnvType("0");
			
			simulateData.onPlaceOrder();
			
			return true;
		} else {
			if(market.compareTo(BasePacket.MARKET_HK) == 0)
			{
		    	BasePacket bp = new BasePacket(Protocols.HK_PLACE_ORDER);
		    	
		    	PlaceOrderParam placeOrderParam = new PlaceOrderParam(cookie, getClientEnvType());
		    	placeOrderParam.setOrderSide(orderSide);
		    	placeOrderParam.setStockCode(stockCode);
		    	placeOrderParam.setPrice(price);
		    	placeOrderParam.setOrderType(PlaceOrderParam.ORDER_TYPE_ENHANCED_LIMIT);
		    	placeOrderParam.setQty(qty);
		    	
		    	bp.setReqParam(placeOrderParam);
		    	
		    	return FutuClient.sendData(bp.toString());
			} else if(market.compareTo(BasePacket.MARKET_US) == 0)
			{
				return false;
			}
			
			return false;
		}
	}
	
	/**
	 * 修改定单价格及数量
	 *  
	 * @param cookie	请求识别码
	 * @param market	市场
	 * @param localId	本地 ID
	 * @param orderId	订单 ID
	 * @param orderStatus	订单状态
	 * @return 成功与否
	 */
	public boolean changeOrder(String cookie, String market, String localId, String orderId, String price, String qty)
	{
    	packetCounter.addPacketCop();
		if(isSimutlateEnable())
		{
			for(HKOrderItem item : simulateData.qolr.getHKOrderArr())
			{
				if(item.getOrderID().compareTo(orderId) == 0 || item.getLocalID().compareTo(localId) == 0)
				{
					item.setPrice(price);
					item.setQty(qty);
					item.setStatus(HKOrderItem.ORDER_STATUS_WAIT_DEAL + "");
					simulateData.qolr.setCookie(cookie);
				}
			}
			
			simulateData.cor = new ChangeOrderResult();
	
			simulateData.cor.setCookie(cookie);
			simulateData.cor.setLocalID(cookie);
			simulateData.cor.setOrderID(cookie);
			simulateData.cor.setSvrResult("0");
			simulateData.cor.setEnvType("0");
	
			simulateData.onChangeOrder();
	
			return true;
		}
		
		if(market.compareTo(BasePacket.MARKET_HK) == 0)
		{
	    	BasePacket bp = new BasePacket(Protocols.HK_CHANGE_ORDER);
	    	
	    	ChangeOrderParam changeOrderParam = new ChangeOrderParam(cookie, getClientEnvType());
	    	changeOrderParam.setLocalID(localId);
	    	changeOrderParam.setOrderID(orderId);
	    	changeOrderParam.setPrice(price);
	    	changeOrderParam.setQty(qty);
	    	
	    	bp.setReqParam(changeOrderParam);
	    	
	    	return FutuClient.sendData(bp.toString());
		} else if(market.compareTo(BasePacket.MARKET_US) == 0)
		{
			return false;
		}
		
		return false;
	}
	
	/**
	 * 设置定单状态 (取消,删除,生效,失效).
	 * 请注意与修改定单的区别
	 * 
	 * @param cookie	请求识别码
	 * @param market	市场
	 * @param localId	本地 ID
	 * @param orderId	订单 ID
	 * @param orderStatus	订单状态
	 * @return 成功与否
	 */
	public boolean setOrderStatus(String cookie,String market, String localId, String orderId, String orderStatus)
	{
    	packetCounter.addPacketSosp();
		if(market.compareTo(BasePacket.MARKET_HK) == 0)
		{
	    	BasePacket bp = new BasePacket(Protocols.HK_SET_ORDER);
	    	
	    	SetOrderStatusParam setOrderStatusParam = new SetOrderStatusParam(cookie, getClientEnvType());
	    	setOrderStatusParam.setLocalID(localId);
	    	setOrderStatusParam.setOrderID(orderId);
	    	setOrderStatusParam.setSetOrderStatus(orderStatus);
	    	
	    	bp.setReqParam(setOrderStatusParam);
	    	
	    	return FutuClient.sendData(bp.toString());
		} else if(market.compareTo(BasePacket.MARKET_US) == 0)
		{
			return false;
		}
		
		return false;
	}
	
	/**
	 * 取回账号信息
	 * 
	 * @param cookie	请求识别码
	 * @param market	市场
	 * @return 成功与否
	 */
	public boolean getAccountInfo(String cookie,String market)
	{
    	packetCounter.addPacketQaip();
		if(isSimutlateEnable())
		if(sendSimulateData(Protocols.HK_QUERY_ACCOUNT))
			return true;
		
		if(market.compareTo(BasePacket.MARKET_HK) == 0)
		{
	    	BasePacket bp = new BasePacket(Protocols.HK_QUERY_ACCOUNT);

	    	BaseAccountParam baseAccountParam = new BaseAccountParam(cookie, getClientEnvType());
	    	
	    	bp.setReqParam(baseAccountParam);
	    	
	    	return FutuClient.sendData(bp.toString());
		} else if(market.compareTo(BasePacket.MARKET_US) == 0)
		{
			return false;
		}
		
		return false;
	}
	
	/**
	 * 取回执仓列表
	 * 
	 * @param cookie	请求识别码
	 * @param market	市场
	 * @return 成功与否
	 */
	public boolean getStockList(String cookie,String market)
	{
    	packetCounter.addPacketQslp();
		if(isSimutlateEnable())
		if(sendSimulateData(Protocols.HK_QUERY_STOCKS))
			return true;
		
		if(market.compareTo(BasePacket.MARKET_HK) == 0)
		{
	    	BasePacket bp = new BasePacket(Protocols.HK_QUERY_STOCKS);
	    	BaseAccountParam baseAccountParam = new BaseAccountParam(cookie, getClientEnvType());
	    	bp.setReqParam(baseAccountParam);
	    	
	    	return FutuClient.sendData(bp.toString());
		} else if(market.compareTo(BasePacket.MARKET_US) == 0)
		{
			return false;
		}
		
		return false;
	}
	
	/**
	 * 取回订单列表信息
	 * 
	 * @param cookie	请求识别码
	 * @param market	市场
	 * @return 成功与否
	 */
	public boolean getOrderList(String cookie,String market)
	{
    	packetCounter.addPacketQolp();
		if(isSimutlateEnable())
		if(sendSimulateData(Protocols.HK_QUERY_ORDERS))
			return true;
		
		BasePacket bp;
		
		if(market.compareTo(BasePacket.MARKET_HK) == 0)
		{
			bp = new BasePacket(Protocols.HK_QUERY_ORDERS);
	    	BaseAccountParam baseAccountParam = new BaseAccountParam(cookie, getClientEnvType());
	    	bp.setReqParam(baseAccountParam);
	    	
	    	return FutuClient.sendData(bp.toString());
		} else if(market.compareTo(BasePacket.MARKET_US) == 0)
		{
			return false;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param cookie	请求识别码
	 * @param market	市场
	 * @return 成功与否
	 */
	public boolean unlockTrade(String cookie)
	{
    	packetCounter.addPacketUtp();
		if(isSimutlateEnable())
		if(sendSimulateData(Protocols.UNLOCK_TRADE))
			return true;
		
		BasePacket bp = new BasePacket(Protocols.UNLOCK_TRADE);
    	UnlockTradeParam unlockTradeParam = new UnlockTradeParam(cookie, getClientEnvType());
    	unlockTradeParam.setPassword(pw);
    	bp.setReqParam(unlockTradeParam);
    	return FutuClient.sendData(bp.toString());
	}
	
	public boolean isSimutlateEnable()
	{
		if(simulateData == null)
			return false;
		
		return simulateData.enabled;
	}
	
	public boolean sendSimulateData(Protocols protocol)
	{
		if(simulateData == null || isSimutlateEnable() == false)
			return false;
		
		if(protocol.getprotocolCode().compareTo(Protocols.HK_QUERY_ACCOUNT.getprotocolCode()) == 0)
		{
			simulateData.onAccountInfo();
			return true;
		} else if(protocol.getprotocolCode().compareTo(Protocols.HK_QUERY_ORDERS.getprotocolCode()) == 0)
		{
			simulateData.onOrderList();
			return true;
		} else if(protocol.getprotocolCode().compareTo(Protocols.HK_QUERY_STOCKS.getprotocolCode()) == 0)
		{
			simulateData.onStockList();
			return true;
		} else if(protocol.getprotocolCode().compareTo(Protocols.STOCK_PRICE.getprotocolCode()) == 0)
		{
			simulateData.onStockPrice();
			return true;
		} else if(protocol.getprotocolCode().compareTo(Protocols.GEAR_LIST.getprotocolCode()) == 0)
		{
			simulateData.onGearPrice();
			return true;
		}
		
		return false;
	}
}
