package client.model.quote;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 取回证券摆盘数据信息返回
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class GearPriceResult {

	private String Market;				//交易市场
	private String StockCode;			//股票代码
	private List<GearPriceItem> GearArr;	//摆盘数据结点;

	@JsonProperty("GearArr")
	public List<GearPriceItem> getGearArr() {
		return GearArr;
	}

	@JsonProperty("GearArr")
	public void setGearArr(List<GearPriceItem> gearArr) {
		GearArr = gearArr;
	}

	@JsonProperty("Market")
	public String getMarket() {
		return Market;
	}

	@JsonProperty("Market")
	public void setMarket(String market) {
		Market = market;
	}

	@JsonProperty("StockCode")
	public String getStockCode() {
		return StockCode;
	}

	@JsonProperty("StockCode")
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}

	public int compareTo(GearPriceResult gpr) {
		if(Market.compareTo(gpr.getMarket()) != 0) return 1;
		if(StockCode.compareTo(gpr.getStockCode()) != 0) return 1;
		if(GearArr != null && gpr.getGearArr() == null) return 1;
		if(GearArr == null && gpr.getGearArr() != null) return 1;
		if(GearArr.size() != gpr.getGearArr().size()) return 1;
		
		for(int i=0;i<GearArr.size();i++)
		{
			GearPriceItem g1 = GearArr.get(i);
			GearPriceItem g2 = gpr.getGearArr().get(i);
			if(Integer.parseInt(g1.getBuyOrder()) != Integer.parseInt(g2.getBuyOrder()) ||
					Integer.parseInt(g1.getBuyPrice()) != Integer.parseInt(g2.getBuyPrice()) ||
					Integer.parseInt(g1.getBuyVol()) != Integer.parseInt(g2.getBuyVol()) ||
					Integer.parseInt(g1.getSellOrder()) != Integer.parseInt(g2.getSellOrder()) ||
					Integer.parseInt(g1.getSellPrice()) != Integer.parseInt(g2.getSellPrice()) ||
					Integer.parseInt(g1.getSellVol()) != Integer.parseInt(g2.getSellVol()))
			{
				return 1;
			}
		}
		
		return 0;
	}

}
