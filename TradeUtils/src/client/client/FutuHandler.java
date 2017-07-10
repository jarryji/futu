package client.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import client.model.PacketCounter;
import client.model.account.ClientAuthorizeParam;
import client.model.account.QueryAccountInfoResult;
import client.model.account.QueryOrderListResult;
import client.model.account.QueryStockListResult;
import client.model.account.UnlockTradeResult;
import client.model.base.BasePacket;
import client.model.observer.ObserableAccountInfo;
import client.model.observer.ObserableChangeOrder;
import client.model.observer.ObserableGearPrice;
import client.model.observer.ObserableOrderList;
import client.model.observer.ObserablePlaceOrder;
import client.model.observer.ObserableSetOrderStatus;
import client.model.observer.ObserableStockList;
import client.model.observer.ObserableStockPrice;
import client.model.observer.ObserableUnlockTrade;
import client.model.order.ChangeOrderResult;
import client.model.order.PlaceOrderResult;
import client.model.order.SetOrderStatusResult;
import client.model.quote.GearPriceResult;
import client.model.quote.StockPriceResult;

/**
 * 
 * @author jarry
 *
 */
public class FutuHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(FutuHandler.class);
    private static final Gson gson = new Gson();
    
	public static final ObserableGearPrice obserableGearPrice = new ObserableGearPrice();
	public static final ObserableStockPrice obserableStockPrice = new ObserableStockPrice();

	public static final ObserablePlaceOrder obserablePlaceOrder = new ObserablePlaceOrder();
	public static final ObserableChangeOrder obserableChangeOrder = new ObserableChangeOrder();
	public static final ObserableSetOrderStatus obserableSetOrderStatus = new ObserableSetOrderStatus();
	
	public static final ObserableAccountInfo obserableAccountInfo = new ObserableAccountInfo();
	public static final ObserableOrderList obserableOrderList = new ObserableOrderList();
	public static final ObserableStockList obserableStockList = new ObserableStockList();
	
	public static final ObserableUnlockTrade obserableUnlockTrade = new ObserableUnlockTrade();

	public static final PacketCounter packetCounter = new PacketCounter();
	
	public static PacketCounter getPacketcounter() {
		return packetCounter;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		packetCounter.zero();
		logger.info("Server Connected!");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage(), cause);
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		synchronized(this)
		{
			final String startString = "\"RetData\":";
			final String endString = ",\"Version\"";
			String json = message.toString();
			
			if(json.length() == 0)
				return;
			
			packetCounter.addPacketReceived();
			
			//logger.info("Received message " + json);
			if(json.length() > 1024)
			{
				logger.debug("Received message length: " + json.length() + " - " + json.substring(0, 200));
			} else {
				logger.debug("Received message " + json);
			}
			
			int startIndex = -1;
			int endIndex = -1;
			String newJson = "";
			BasePacket bp = null;
	
			if((startIndex = json.indexOf(startString)) > -1)
			{
				startIndex += startString.length();
				endIndex = json.indexOf(endString);
				
//				String part1 = json.substring(0,startIndex);
//				String part2 = json.substring(json.indexOf(endString));
	
				String retData = json.substring(startIndex, endIndex);
				retData = retData.replace("\"", "\\\"");
				newJson = json.substring(0, startIndex) + "\"" + retData + "\"" + json.substring(endIndex);

				bp = BasePacket.fromString(newJson);
				
			} else 
			{
				bp = BasePacket.fromString(json);
			}
			
			if(bp == null)
			{
				System.out.println("Unable to deserialize ");
				return;
			}
			
			if(Integer.parseInt(bp.getErrCode()) != 0)
			{
				System.out.println("Error code: " + bp.getErrCode() + " description: " + bp.getErrDesc());
				packetCounter.addPacketError();
				return;
			}
			
			if(bp.getProtocol().compareTo(BasePacket.CLIENT_AUTHORIZE) == 0)
			{
				onClientAuthorize(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.HK_UNLOCK_TRADE) == 0)
			{
				onUnloadTrade(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.STOCK_PRICE) == 0)
			{
				onStockPrice(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.GEAR_LIST) == 0)
			{
				onGearPrice(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.HK_PLACE_ORDER) == 0)
			{
				onPlaceOrder(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.HK_CHANGE_ORDER) == 0)
			{
				onChangeOrder(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.HK_SET_ORDER) == 0)			//
			{
				onSetOrderStatus(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.HK_QUERY_ACCOUNT) == 0)
			{
				onAccountInfo(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.HK_QUERY_ORDERS) == 0)
			{
				onOrderList(bp);
			} else if(bp.getProtocol().compareTo(BasePacket.HK_QUERY_STOCKS) == 0)
			{
				onStockList(bp);
			}
		}
	}
	
	public void onStockList(BasePacket bp) {
	    QueryStockListResult qslr = gson.fromJson(bp.getRetData(), QueryStockListResult.class);
	    obserableStockList.setQueryStockListResult(qslr);
	    obserableStockList.onUpdate();
	    packetCounter.addPacketQslr();
	}

	public void onOrderList(BasePacket bp) {
	    QueryOrderListResult qolr = gson.fromJson(bp.getRetData(), QueryOrderListResult.class);
	    obserableOrderList.setQueryOrderListResult(qolr);
	    obserableOrderList.onUpdate();
	    packetCounter.addPacketQolr();
	}

	public void onAccountInfo(BasePacket bp) {
	    QueryAccountInfoResult qair = gson.fromJson(bp.getRetData(), QueryAccountInfoResult.class);
	    if(qair == null) return;
	    obserableAccountInfo.setQueryAccountInfoResult(qair);
	    obserableAccountInfo.onUpdate();
	    packetCounter.addPacketQair();
	}

	public void onSetOrderStatus(BasePacket bp) {
	    SetOrderStatusResult ssr = gson.fromJson(bp.getRetData(), SetOrderStatusResult.class);
	    obserableSetOrderStatus.setSetOrderStatusResult(ssr);
	    obserableSetOrderStatus.onUpdate();
	    packetCounter.addPacketSosr();
	}

	public void onChangeOrder(BasePacket bp) {
	    ChangeOrderResult cor = gson.fromJson(bp.getRetData(), ChangeOrderResult.class);
	    obserableChangeOrder.setChangeOrderResult(cor);
	    obserableChangeOrder.onUpdate();
	}

	public void onPlaceOrder(BasePacket bp) {
	    PlaceOrderResult por = gson.fromJson(bp.getRetData(), PlaceOrderResult.class);
	    obserablePlaceOrder.setPlaceOrderResult(por);
	    obserablePlaceOrder.onUpdate();
	    packetCounter.addPacketPor();
	}
	
	/**
	 * 观察者模式通知有需要的线程
	 * 
	 * @param bp
	 */
	public void onGearPrice(BasePacket bp)
	{
	    GearPriceResult gpr = gson.fromJson(bp.getRetData(), GearPriceResult.class);
	    obserableGearPrice.setGearPriceResult(gpr);
	    obserableGearPrice.onUpdate();
	    packetCounter.addPacketGpr();
	}
	
	public void onStockPrice(BasePacket bp)
	{
		StockPriceResult spr = gson.fromJson(bp.getRetData(), StockPriceResult.class);
	    obserableStockPrice.setStockPriceResult(spr);
	    obserableStockPrice.onUpdate();
	    packetCounter.addPacketSpr();
	    logger.info("StockPriceResult: stock={},price={}",spr.getStockCode(),spr.getCur());
	}

	public void onUnloadTrade(BasePacket bp) {
	    UnlockTradeResult utr = gson.fromJson(bp.getRetData(), UnlockTradeResult.class);

	    if(Integer.parseInt(bp.getErrCode()) == 0)
	    	FutuClient.setUnlockedTrade(true);
	    else
	    	FutuClient.setUnlockedTrade(false);
	    
	    obserableUnlockTrade.setUnlockTradeResult(utr);
	    obserableUnlockTrade.onUpdate();
	    packetCounter.addPacketUtr();
	}
	
	public void onClientAuthorize(BasePacket bp)
	{
	    if(Integer.parseInt(bp.getErrCode()) == 0)
	    	FutuClient.getInstance().setClientAuthorized(true);
	    else
	    	FutuClient.getInstance().setClientAuthorized(false);
	}

	@Override
	public void messageSent(IoSession session, Object message)
			throws Exception {
		//logger.info("Sent message " + message);

		packetCounter.addPacketSent();
	}
}