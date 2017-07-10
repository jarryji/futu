package gui.model;

import javax.swing.table.AbstractTableModel;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;

import strategy.BaseStrategy;
import strategy.StrategyManager;
import trade.ObserableTradeOrder;
import client.client.FutuClient;
import client.model.account.HKPositionItem;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
public class StockTableModel extends FadingTableModel implements Observer {

	private static final long serialVersionUID = 4001669411402687601L;
	private static final StrategyManager strategyManager = new StrategyManager();

	String[] columnName =  {
			"证券名称", "代码", "执仓", "成本",
			"现价", "价位跌落", "自动卖出",
			"开盘", "最高",
			"回收价", "报价时间", "定单状态"
			};
	
	private List<StockTableItem> stockTableList = new ArrayList<StockTableItem>();

	public StockTableModel()
	{
//		StockTableItem item = new StockTableItem();
//		item.setStockName("恒指摩通七十牛Q.C");
//		item.setStockCode("27275");
//		item.setHoldVol(100*1000);
//		item.setCostPrice(23);
//		item.setCurPrice(21);
//		item.setLossLimit(1);
//		item.setEnableAuto(true);
//		item.setOpenPrice(21);
//		item.setRetirePrice(0);
//		item.setUpdateTime("12:00");
//		item.setOrderStatus("自动交易中");
//		
//		stockTableList.add(item);
	}
	
	@Override
	public String getColumnName(int column) {
		return columnName[column];
	}
	@Override
	public int getRowCount() {
		synchronized(stockTableList) {
			return stockTableList.size();
		}
	}

	@Override
	public int getColumnCount() {
		return columnName.length;
	}
	
    @Override  
    public Class<?> getColumnClass(int columnIndex)  
    {
    	switch(columnIndex)
		{
		case 5:
			return Integer.class;
		case 6:
			return Boolean.class;
		default:
			return String.class;
		}
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex > stockTableList.size()) return null;
		StockTableItem item = stockTableList.get(rowIndex);
		
		switch(columnIndex)
		{
		case 0:
			return item.getStockName();
		case 1:
			return item.getStockCode();
		case 2:
			return item.getHoldVol();
		case 3:
			return item.getCostPrice();
		case 4:
			return item.getCurPrice();
		case 5:
			return item.getLossLimit();
		case 6:
			return item.isEnableAuto();
		case 7:
			return item.getOpenPrice();
		case 8:
			return item.getClosePrice();
		case 9:
			return item.getRetirePrice();
		case 10:
			return item.getUpdateTime();
		case 11:
			return item.getOrderStatus();
		default:
			return "";
		}
		
	}
	
    @Override  
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)  
    {  
    	StockTableItem item = getStockTableItem(rowIndex);
    	synchronized(item)
    	{
			switch(columnIndex)
			{
			case 5:
				item.setLossLimit((Integer)aValue);
				break;
			case 6:
				item.setEnableAuto((Boolean)aValue);
				break;
			}

			BaseStrategy bs = strategyManager.getStragtegy(0, item.getStockCode());
			if(bs == null) return;
			
			switch(columnIndex)
			{
			case 5:
				bs.setLossLimit((Integer)aValue);
				item.setOrderStatus("修改跌落价位");
				break;
			case 6:
				bs.setEnabled((Boolean)aValue);
				item.setOrderStatus("设置生效状态");
		        fireTableCellUpdated(rowIndex, 11);
				break;
			}
    	}
		
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex >= 5 && columnIndex <= 6)
			return true;
		else
			return false;
	}
	
	
	public void fade()
    {
//		fireTableCellUpdated(0, 1);
    }

    public boolean isSuspect()
    {
    	return false;
    }

    public boolean isUpdated(int rowIndex, int columnIndex)
    {
    	return true;

    }
    
    public StockTableItem getStockTableItem(String stockCode) {
    	synchronized(stockTableList)
    	{
    		for(StockTableItem item : stockTableList)
    		{
    			if(item.getStockCode().compareTo(stockCode) == 0)
    				return item;
    		}
    	}
    	return null;
    }
    
    public StockTableItem getStockTableItem(int rowIndex) {
    	synchronized(stockTableList)
    	{
    		return stockTableList.get(rowIndex);
    	}
    }
    

	protected void onGearPriceResult(GearPriceResult gpr) {}
	protected void onStockPriceResult(StockPriceResult spr) {
		StockTableItem item;
		if((item = getStockTableItem(spr.getStockCode())) == null) return;
		
		item.setCurPrice(Integer.parseInt(spr.getCur()));
		item.setClosePrice(Integer.parseInt(spr.getClose()));
		item.setHighPrice(Integer.parseInt(spr.getHigh()));

		item.setLowPrice(Integer.parseInt(spr.getLow()));
		item.setOpenPrice(Integer.parseInt(spr.getOpen()));
		item.setUpdateTime(spr.getTime());
		
		synchronized(stockTableList)
		{
			int indexRow = stockTableList.indexOf(item);
			fireTableCellUpdated(indexRow, 4);
			fireTableCellUpdated(indexRow, 8);
			fireTableCellUpdated(indexRow, 7);
			fireTableCellUpdated(indexRow, 10);
		}
		
	}
	protected void onPlaceOrderResult(PlaceOrderResult por) {}
	protected void onChangeOrderResult(ChangeOrderResult cor) {}
	protected void onSetOrderStatusResult(SetOrderStatusResult sosr) {}
	protected void onAccountInfoResult(QueryAccountInfoResult qair) {}
	protected void onOrderListResult(QueryOrderListResult qosr) {}
	
	protected void onStockListResult(QueryStockListResult qslr) {
		synchronized(qslr){
			boolean bInsert = false;
			List<HKPositionItem> positionList = qslr.getHKPositionArr();
			if(positionList == null) return;
			StockTableItem item = null;
			
			for(HKPositionItem positionItem: positionList)
			{
				if((item = getStockTableItem(positionItem.getStockCode())) == null)
				{
					item = new StockTableItem();
					item.setStockName(positionItem.getStockName());
					item.setStockCode(positionItem.getStockCode());
					item.setHoldVol(Integer.parseInt(positionItem.getCanSellQty()));
					item.setCostPrice(Integer.parseInt(positionItem.getCostPriceValid()) != 0 ? Integer.parseInt(positionItem.getCostPrice()) : 0);
					item.setCurPrice(0);
					item.setLossLimit(2);
					item.setEnableAuto(false);
					item.setOpenPrice(0);
					item.setRetirePrice(0);
					item.setUpdateTime("");
					item.setOrderStatus("新同步");
					bInsert = true;
					synchronized(stockTableList) {
						stockTableList.add(item);
						BaseStrategy bs = null;
						
						if((bs = strategyManager.createStrategy(0, BasePacket.MARKET_HK, item.getStockCode())) != null)
							bs.setLossLimit(2);
						
					}
				} else {
					item.setHoldVol(Integer.parseInt(positionItem.getCanSellQty()));
					item.setCostPrice(Integer.parseInt(positionItem.getCostPriceValid()) != 0 ? Integer.parseInt(positionItem.getCostPrice()) : 0);
					item.setOrderStatus("同步执仓");
				}
			}
			
			if(bInsert) fireTableDataChanged();
		}
	}

	
	protected void onUnlockTradeResult(UnlockTradeResult utr) {}

	@Override
	public void update(Observable o, Object arg) {
		synchronized (this)
		{
			if(ObserableTradeOrder.class.isInstance(o))
			{
				ObserableTradeOrder oto = (ObserableTradeOrder)o;
				
				StockTableItem item;
				if((item = getStockTableItem(oto.getStockCode())) == null) return;
				item.setOrderStatus(oto.getShortMessage());
				synchronized(stockTableList)
				{
					int rowIndex = stockTableList.indexOf(item);
					fireTableCellUpdated(rowIndex, 11);
				}
				
			} else if(ObserableGearPrice.class.isInstance(o))
			{
				//处理更新摆盘数据
				ObserableGearPrice ogp = (ObserableGearPrice)o;
				synchronized (ogp)
				{
					GearPriceResult gpr = ogp.getGearPriceResult();
					onGearPriceResult(gpr);
				}
			} else if(ObserableStockPrice.class.isInstance(o))
			{
				//处理更新股票数据
				ObserableStockPrice osp = (ObserableStockPrice)o;
				synchronized (osp)
				{
					StockPriceResult spr = osp.getStockPriceResult();
					onStockPriceResult(spr);
				}
			} else if(ObserablePlaceOrder.class.isInstance(o))
			{
				ObserablePlaceOrder obserable = (ObserablePlaceOrder)o;
				synchronized (obserable)
				{
					PlaceOrderResult por = obserable.getPlaceOrderResult();

					onPlaceOrderResult(por);
				}
			} else if(ObserableChangeOrder.class.isInstance(o))
			{
				ObserableChangeOrder obserable = (ObserableChangeOrder)o;
				synchronized (obserable)
				{
					ChangeOrderResult cor = obserable.getChangeOrderResult();

					onChangeOrderResult(cor);
				}
			} else if(ObserableSetOrderStatus.class.isInstance(o))
			{
				ObserableSetOrderStatus obserable = (ObserableSetOrderStatus)o;
				synchronized (obserable)
				{
					SetOrderStatusResult sosr = obserable.getSetOrderStatusResult();

					onSetOrderStatusResult(sosr);
				}
			} else if(ObserableAccountInfo.class.isInstance(o))
			{
				ObserableAccountInfo obserable = (ObserableAccountInfo)o;
				synchronized (obserable)
				{
					QueryAccountInfoResult qair = obserable.getQueryAccountInfoResult();

					onAccountInfoResult(qair);
				}
			} else if(ObserableOrderList.class.isInstance(o))
			{
				ObserableOrderList obserable = (ObserableOrderList)o;
				synchronized (obserable)
				{
					QueryOrderListResult qolr = obserable.getQueryOrderListResult();

					onOrderListResult(qolr);
				}
			} else if(ObserableStockList.class.isInstance(o))
			{
				ObserableStockList obserable = (ObserableStockList)o;
				synchronized (obserable)
				{
					QueryStockListResult qslr = obserable.getQueryStockListResult();

					onStockListResult(qslr);
				}
			} else if(ObserableUnlockTrade.class.isInstance(o))
			{
				ObserableUnlockTrade obserable = (ObserableUnlockTrade)o;
				synchronized (obserable)
				{
					UnlockTradeResult utr = obserable.getUnlockTradeResult();

					onUnlockTradeResult(utr);
				}
			}
		}
	}
}
