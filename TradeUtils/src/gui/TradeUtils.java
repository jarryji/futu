package gui;


import gui.model.StockTableModel;
import gui.model.TableCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JList;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;

import client.client.FutuClient;
import client.client.FutuHandler;
import client.client.SimulateData;
import client.model.PacketCounter;
import client.model.account.HKOrderItem;
import client.model.base.BaseAccountParam;
import client.model.base.BasePacket;
import client.model.base.BasePacket.Protocols;
import client.model.order.PlaceOrderParam;
import client.model.quote.GearPriceParam;
import client.model.quote.StockPriceParam;
import client.model.quote.StockPriceResult;

import javax.swing.JButton;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strategy.SimpleStopLoss;
import strategy.StrategyManager;
import test.ProtocolTest;
import trade.TradeManager;
import trade.TradeOrder;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JTextArea;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TradeUtils extends Thread implements Observer {
	private static final Logger logger = LoggerFactory.getLogger(TradeUtils.class);
	private static FutuClient futuClient;
	private TradeManager tradeManager = TradeManager.getInstance();
	private SimulateData simulateData = new SimulateData();
	private StockTableModel stockTableModel = new StockTableModel();
	
	private JFrame frame;
	private JTextField textLowerBuyer;
	private JTextField textLossLimit;
	public static Thread tThread = null;
	private static Object lock = new Object();
	private JTextField textPrice;
	private JTextField textOrderPrice;

	private int selectRow = -1;
	private JLabel labelReceived = new JLabel("接收: 0");	
	private JLabel labelSent = new JLabel("发送: 0");	
	private JLabel labelSpr = new JLabel("报价: 0");	
	private JLabel labelGpr = new JLabel("摆盘: 0");	
	private JLabel labelPor = new JLabel("卖出: 0");	
	private JLabel labelCor = new JLabel("改单: 0");	
	private JLabel labelQslr = new JLabel("查执仓: 0");	
	private JLabel labelQolr = new JLabel("查订单: 0");	
	private JLabel labelQair = new JLabel("查账户: 0");	
	private JLabel labelPacketError = new JLabel("错误: 0");
	private JTextField textServerAddress;
	private JTextField textServerPort;
	private JTable stockTable = new JTable(stockTableModel);
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		final TradeUtils window = new TradeUtils();
		Thread t = new Thread()
		{
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				while(window.frame.isVisible())
				{
					synchronized(lock)
					{
	                    try {
	    					sleep(200);
	                        lock.wait();
	                    } catch (InterruptedException e) {
	                    }
					}
				}

                System.out.println("Exiting");
			}
		};
		
		t.start();
		
		window.frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent arg0) {
	            synchronized (lock) {
	            	if(TradeUtils.tThread != null) TradeUtils.tThread.interrupt();
	            	if(TradeUtils.futuClient != null) TradeUtils.futuClient.interrupt();
	                lock.notify();
	            	window.frame.setVisible(false);
	            }
	        }
	    });

	    try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getMarket()
	{
		return BasePacket.MARKET_HK;
	}
	
	public String getNewCookie()
	{
		return futuClient.getNewCookie();
	}

	/**
	 * Create the application.
	 */
	public TradeUtils() {
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("conf/log4j.properties"));
		//serverAddress = "192.168.1.200";
		//serverPort = 59988;
		futuClient = new FutuClient();
		FutuClient.simulateData = simulateData;
		simulateData.enabled = false;
		futuClient.setClientEnvType(BaseAccountParam.CLIENT_ENV_TYPE_REAL);
		tradeManager.setFutuClient(futuClient);
//		simpleStopLoss.setFutuClient(futuClient);
//
//		simpleStopLoss.setMarket(getMarket());
//		simpleStopLoss.setStockCode(stockCode);
//		simpleStopLoss.setLossLimit(1);
//		simpleStopLoss.start();

		futuClient.addObserver(tradeManager);
//		futuClient.addObserver(simpleStopLoss);
		futuClient.addObserver(this);
		futuClient.addObserver(stockTableModel);
		
//		futuClient.getAccountInfo(this.getNewCookie());
//		futuClient.getStockList(this.getNewCookie(), this.getMarket());
//		futuClient.getOrderList(this.getNewCookie(), this.getMarket());
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("交易助手");
		frame.setBounds(100, 100, 1018, 545);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
		} catch (InstantiationException e1) {
		} catch (IllegalAccessException e1) {
		} catch (UnsupportedLookAndFeelException e1) {
		}

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
		);
		
		JPanel panelTrade = new JPanel();
		tabbedPane.addTab("交易信息", null, panelTrade, null);
		
		JLabel labelLossLimit = new JLabel("跌幅超过价位:");
		
		JLabel label_1 = new JLabel("低于买家价位:");
		
		JLabel label_2 = new JLabel("交易类型:");
		
		JComboBox comboBoxTradeType = new JComboBox();
		
		textLowerBuyer = new JTextField();
		textLowerBuyer.setColumns(10);
		
		textLossLimit = new JTextField();
		textLossLimit.setColumns(10);
		
		final JCheckBox checkEnableAuto = new JCheckBox("自动交易");
		
		JCheckBox checkAutoAdjust = new JCheckBox("自动调整价位");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JButton btnTest = new JButton("TestPrice");
		
		btnTest.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	textPrice.setText((Integer.parseInt(textPrice.getText()) - 1) + ""); 
				simulateData.spr.setCur(textPrice.getText());
            }
        }));
		
		textPrice = new JTextField();
		textPrice.setText(simulateData.spr.getCur());
		textPrice.setColumns(10);
		
		textOrderPrice = new JTextField();
		textOrderPrice.setText("60");
		textOrderPrice.setColumns(10);
		
		JButton btnTestorder = new JButton("TestOrder");
		btnTestorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

            	textOrderPrice.setText((Integer.parseInt(textOrderPrice.getText()) + 1) + "");
				simulateData.spr.setCur(textPrice.getText());
				
				for(HKOrderItem orderItem : simulateData.qolr.getHKOrderArr())
				{
					if(orderItem.getOrderSide().compareTo(PlaceOrderParam.ORDER_SIDE_SELL) != 0) continue;
					if(Integer.parseInt(orderItem.getStatus()) == HKOrderItem.ORDER_STATUS_WAIT_DEAL ||
							Integer.parseInt(orderItem.getStatus()) == HKOrderItem.ORDER_STATUS_PARTIAL_DEAL)
					{
						orderItem.setPrice((Integer.parseInt(textOrderPrice.getText()) + 1) + "");
					}
				}
			}
		});
		stockTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((selectRow = stockTable.getSelectedRow()) == -1) { return; }
				if (e.getButton() == MouseEvent.BUTTON1) {
					textLossLimit.setText(stockTableModel.getValueAt(selectRow, 5).toString());
					checkEnableAuto.setSelected((boolean) stockTableModel.getValueAt(selectRow, 6));
				}
			}
		});
		
		stockTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		stockTable.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(stockTable);
		stockTable.setFont(new Font("Courier", Font.PLAIN, 12));
		
		prepareTable(stockTable);
		GroupLayout gl_panelTrade = new GroupLayout(panelTrade);
		gl_panelTrade.setHorizontalGroup(
			gl_panelTrade.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTrade.createSequentialGroup()
					.addGap(6)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
					.addGap(12)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(labelLossLimit)
							.addGap(12)
							.addComponent(textLossLimit, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(checkEnableAuto))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(label_1)
							.addGap(12)
							.addComponent(textLowerBuyer, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(checkAutoAdjust))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(label_2)
							.addGap(12)
							.addComponent(comboBoxTradeType, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(textPrice, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(btnTest, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(textOrderPrice, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(btnTestorder, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(labelSpr, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(labelGpr, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(labelPor, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(labelCor, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(labelQslr, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(labelQolr, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(labelQair, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(labelPacketError, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addComponent(labelSent, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(labelReceived, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)))
					.addGap(7))
		);
		gl_panelTrade.setVerticalGroup(
			gl_panelTrade.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTrade.createSequentialGroup()
					.addGap(11)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
					.addGap(6))
				.addGroup(gl_panelTrade.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addGap(6)
							.addComponent(labelLossLimit))
						.addComponent(textLossLimit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addGap(2)
							.addComponent(checkEnableAuto)))
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addGap(6)
							.addComponent(label_1))
						.addComponent(textLowerBuyer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addGap(2)
							.addComponent(checkAutoAdjust)))
					.addGap(2)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addGap(4)
							.addComponent(label_2))
						.addComponent(comboBoxTradeType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(24)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addComponent(textPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addGap(1)
							.addComponent(btnTest)))
					.addGap(10)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addComponent(textOrderPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelTrade.createSequentialGroup()
							.addGap(1)
							.addComponent(btnTestorder)))
					.addGap(182)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addComponent(labelSpr)
						.addComponent(labelGpr))
					.addGap(6)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addComponent(labelPor)
						.addComponent(labelCor))
					.addGap(6)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addComponent(labelQslr)
						.addComponent(labelQolr))
					.addGap(6)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addComponent(labelQair)
						.addComponent(labelPacketError))
					.addGap(6)
					.addGroup(gl_panelTrade.createParallelGroup(Alignment.LEADING)
						.addComponent(labelSent)
						.addComponent(labelReceived)))
		);
		panelTrade.setLayout(gl_panelTrade);
		
		stockTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
//					String newvalue = stockTable.getValueAt(e.getFirstRow(),
//							e.getColumn()).toString();
//					System.out.println(newvalue);
				}
			}
		});
		
		JPanel panelSetup = new JPanel();
		tabbedPane.addTab("系统配置", null, panelSetup, null);
		
		JLabel label_4 = new JLabel("服务器地址:");
		
		JLabel label_5 = new JLabel("服务器端口:");
		
		textServerAddress = new JTextField();
		textServerAddress.setText(FutuClient.getConfig().getServerAddress());
		textServerAddress.setColumns(17);
		
		textServerPort = new JTextField();
		textServerPort.setText(FutuClient.getConfig().getServerPort() + "");
		textServerPort.setColumns(5);
		
		JButton btnConnectServer = new JButton("连接服务器");
		btnConnectServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				futuClient.setServerAddress(textServerAddress.getText());
				futuClient.setServerPort(Integer.parseInt(textServerPort.getText()));
			}
		});
		GroupLayout gl_panelSetup = new GroupLayout(panelSetup);
		gl_panelSetup.setHorizontalGroup(
			gl_panelSetup.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSetup.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelSetup.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelSetup.createSequentialGroup()
							.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textServerAddress, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelSetup.createSequentialGroup()
							.addComponent(label_5, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelSetup.createParallelGroup(Alignment.LEADING)
								.addComponent(btnConnectServer)
								.addComponent(textServerPort, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(813, Short.MAX_VALUE))
		);
		gl_panelSetup.setVerticalGroup(
			gl_panelSetup.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSetup.createSequentialGroup()
					.addGap(40)
					.addGroup(gl_panelSetup.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_4)
						.addComponent(textServerAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelSetup.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_5)
						.addComponent(textServerPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnConnectServer)
					.addContainerGap(328, Short.MAX_VALUE))
		);
		panelSetup.setLayout(gl_panelSetup);
		
		JPanel panelLog = new JPanel();
		tabbedPane.addTab("日志", null, panelLog, null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JLabel label = new JLabel("交易日志");
		
		JLabel label_6 = new JLabel("信息日志");
		GroupLayout gl_panelLog = new GroupLayout(panelLog);
		gl_panelLog.setHorizontalGroup(
			gl_panelLog.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLog.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelLog.createParallelGroup(Alignment.LEADING)
						.addComponent(label)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelLog.createParallelGroup(Alignment.LEADING)
						.addComponent(label_6, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelLog.setVerticalGroup(
			gl_panelLog.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelLog.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelLog.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(label_6))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelLog.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JTextArea txtrTest = new JTextArea();
		txtrTest.setText("还没有做界面");
		scrollPane_1.setViewportView(txtrTest);
		
		JTextArea txtrInfo = new JTextArea();
		txtrInfo.setText("还没有做界面");
		scrollPane_2.setViewportView(txtrInfo);
		panelLog.setLayout(gl_panelLog);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	private void prepareTable(JTable table)
	{
		TableCellRenderer tableCellRenderer = new TableCellRenderer();
		
	    for (int i = 0; i < table.getColumnCount(); ++i)
	    {
	    	TableColumn column = table.getColumnModel().getColumn(i);
	    	//if(i<=1 || i>=6)
	    	{
	    		column.setCellRenderer(tableCellRenderer);
	    	}

	    	switch (i)
	    	{
	    	case 0:
	    		column.setPreferredWidth(120);
	    	break;
	    	case 1:
	    		column.setPreferredWidth(55);
	    	break;
	    	case 2:
	    		column.setPreferredWidth(45);
	    	break;
	    	case 3:
	    		column.setPreferredWidth(45);
	    	break;
	    	case 4:
	    		column.setPreferredWidth(45);
	    	break;
	    	case 5:
	    		column.setPreferredWidth(55);
	    	break;
	    	case 6:
	    		column.setPreferredWidth(50);
	    		column.setCellRenderer(table.getDefaultRenderer(Boolean.class));
	    	break;
	    	case 7:
	    		column.setPreferredWidth(50);
	    	break;
	    	case 10:
	    		column.setPreferredWidth(55);
	    	break;
	    	case 11:
	    		column.setPreferredWidth(90);
		    break;
	    	default:
	    		column.setPreferredWidth(45);
	    	}

    		column.setMaxWidth((int)(column.getPreferredWidth() * 1.5));
	    }
	}
	
	
	
	/*
	    **  Color cell background
	    */
	    class ColorRenderer implements ActionListener
	    {
	        private JTable stockTable;
	        private AbstractTableModel model;
	        private Map colors;
	        private boolean isBlinking = true;
	        private Timer timer;
	        private Point location;
	 
	        public ColorRenderer(JTable table)
	        {
	            this.stockTable = table;
	            model = (AbstractTableModel)table.getModel();
	            colors = new HashMap();
	            location = new Point();
	        }
	 
	        public void setBackground(Component c, int row, int column)
	        {
	            //  Don't override the background color of a selected cell
	 
	            if ( stockTable.isCellSelected(row, column) ) return;
	 
	            //  The default render does not reset the background color
	            //  that was set for the previous cell, so reset it here
	 
	            if (c instanceof DefaultTableCellRenderer)
	            {
	                c.setBackground( stockTable.getBackground() );
	            }
	 
	            //  Don't highlight this time
	 
	            if ( !isBlinking ) return;
	 
	            //  In case columns have been reordered, convert the column number
	 
	            column = stockTable.convertColumnIndexToModel(column);
	 
	            //  Get cell color
	 
	            Object key = getKey(row, column);
	            Object o = colors.get( key );
	 
	            if (o != null)
	            {
	                c.setBackground( (Color)o );
	                return;
	            }
	 
	            //  Get row color
	 
	            key = getKey(row, -1);
	            o = colors.get( key );
	 
	            if (o != null)
	            {
	                c.setBackground( (Color)o );
	                return;
	            }
	 
	            //  Get column color
	 
	            key = getKey(-1, column);
	            o = colors.get( key );
	 
	            if (o != null)
	            {
	                c.setBackground( (Color)o );
	                return;
	            }
	 
	        }
	 
	        public void setCellColor(int row, int column, Color color)
	        {
	            Point key = new Point(row, column);
	            colors.put(key, color);
	        }
	 
	        public void setColumnColor(int column, Color color)
	        {
	            setCellColor(-1, column, color);
	        }
	 
	        public void setRowColor(int row, Color color)
	        {
	            setCellColor(row, -1, color);
	        }
	 
	        private Object getKey(int row, int column)
	        {
	            location.x = row;
	            location.y = column;
	            return location;
	        }
	 
	        public void startBlinking(int interval)
	        {
	            timer = new Timer(interval, this);
	            timer.start();
	        }
	 
	        public void stopBlinking()
	        {
	            timer.stop();
	        }

			@Override
			public void actionPerformed(ActionEvent e) {
	        {
	            isBlinking = !isBlinking;
	 
	            Iterator it = colors.keySet().iterator();
	 
	            while ( it.hasNext() )
	            {
	                Point key = (Point)it.next();
	                int row = key.x;
	                int column = key.y;
	 
	                if (column == -1)
	                {
	                    model.fireTableRowsUpdated(row, row);
	                }
	                else if (row == -1)
	                {
	                    int rows = stockTable.getRowCount();
	 
	                    for (int i = 0; i < rows; i++)
	                    {
	                        model.fireTableCellUpdated(i, column);
	                    }
	                }
	                else
	                {
	                    model.fireTableCellUpdated(row, column);
	                }
	            }
	        }
	    }
	 }



		@Override
		public void update(Observable o, Object arg) {
			FutuClient.getClientHandler();
			PacketCounter packetCounter = FutuHandler.getPacketcounter();
			labelReceived.setText("接收: " + packetCounter.getPacketReceived());
			labelSent.setText("发送: " + packetCounter.getPacketSent());
			labelSpr.setText("报价: " + packetCounter.getPacketSpr());
			labelGpr.setText("摆盘: " + packetCounter.getPacketGpr());
			labelPor.setText("卖出: " + packetCounter.getPacketPor());
			labelCor.setText("改单: " + packetCounter.getPacketCor());
			labelQslr.setText("查执仓: " + packetCounter.getPacketQslr());
			labelQolr.setText("查订单: " + packetCounter.getPacketQolr());
			labelQair.setText("查账户: " + packetCounter.getPacketQair());
			labelPacketError.setText("错误: " + packetCounter.getPacketError());
		}
}
