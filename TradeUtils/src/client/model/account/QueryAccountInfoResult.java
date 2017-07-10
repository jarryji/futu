package client.model.account;
/**
 * 查询账号信息返回数据
 * 
 * @author jarry
 * @since 2016/9/30
 * @version 1.0
 */
public class QueryAccountInfoResult {
	private String Cookie;			//操作标识
	private String Power;			//购买力
	private String ZCJZ;			//资产净值
	private String ZQSZ;			//证券市值
	private String XJJY;			//现金结余
	private String KQXJ;			//可取现金   
	private String DJZJ;			//冻结资金
	private String ZSJE;			//追收金额
	private String ZGJDE;			//最高借贷额
	private String YYJDE;			//已用信贷额
	private String GPBZJ;			//股票保证金
	public String getCookie() {
		return Cookie;
	}
	public void setCookie(String cookie) {
		Cookie = cookie;
	}
	public String getPower() {
		return Power;
	}
	public void setPower(String power) {
		Power = power;
	}
	public String getZCJZ() {
		return ZCJZ;
	}
	public void setZCJZ(String zCJZ) {
		ZCJZ = zCJZ;
	}
	public String getZQSZ() {
		return ZQSZ;
	}
	public void setZQSZ(String zQSZ) {
		ZQSZ = zQSZ;
	}
	public String getXJJY() {
		return XJJY;
	}
	public void setXJJY(String xJJY) {
		XJJY = xJJY;
	}
	public String getKQXJ() {
		return KQXJ;
	}
	public void setKQXJ(String kQXJ) {
		KQXJ = kQXJ;
	}
	public String getDJZJ() {
		return DJZJ;
	}
	public void setDJZJ(String dJZJ) {
		DJZJ = dJZJ;
	}
	public String getZSJE() {
		return ZSJE;
	}
	public void setZSJE(String zSJE) {
		ZSJE = zSJE;
	}
	public String getZGJDE() {
		return ZGJDE;
	}
	public void setZGJDE(String zGJDE) {
		ZGJDE = zGJDE;
	}
	public String getYYJDE() {
		return YYJDE;
	}
	public void setYYJDE(String yYJDE) {
		YYJDE = yYJDE;
	}
	public String getGPBZJ() {
		return GPBZJ;
	}
	public void setGPBZJ(String gPBZJ) {
		GPBZJ = gPBZJ;
	}
	
}
