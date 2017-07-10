package client.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import client.model.base.BaseAccountParam;
/**
 * 查询订单列表返回
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class QueryOrderListResult extends BaseAccountParam {
	
	private List<HKOrderItem> HKOrderArr;

	public QueryOrderListResult(String cookie, String envType)
	{
		super(cookie, envType);
	}

	@JsonProperty("HKOrderArr")
	public List<HKOrderItem> getHKOrderArr() {
		return HKOrderArr;
	}

	@JsonProperty("HKOrderArr")
	public void setHKOrderArr(List<HKOrderItem> hkOrderArr) {
		HKOrderArr = hkOrderArr;
	}

}
