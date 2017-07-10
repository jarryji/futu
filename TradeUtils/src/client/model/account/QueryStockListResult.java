package client.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import client.model.base.BaseAccountParam;
/**
 * 查询执仓证券列表信息返回
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class QueryStockListResult  extends BaseAccountParam {
	public QueryStockListResult(String cookie, String envType)
	{
		super(cookie, envType);
	}

	private List<HKPositionItem> HKPositionArr;

	@JsonProperty("HKPositionArr")
	public List<HKPositionItem> getHKPositionArr() {
		return HKPositionArr;
	}

	@JsonProperty("HKPositionArr")
	public void setHKPositionArr(List<HKPositionItem> hKPositionArr) {
		HKPositionArr = hKPositionArr;
	}

}
