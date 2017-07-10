package client.model;

public enum ErrorCode {
	PROTO_ERR_NO_ERROR				(0, "正常")
	,PROTO_ERR_UNKNOWN_ERROR		(400, "未知错误")
	,PROTO_ERR_VER_NOT_SUPPORT		(401, "版本不支持")
	,PROTO_ERR_STOCK_NOT_FIND		(402, "股票未找到")
	,PROTO_ERR_COMMAND_NOT_SUPPORT	(403, "不支持的命令")
	,PROTO_ERR_PARAM_ERR			(404, "错误的参数")
	,PROTO_ERR_NOT_AUTHORIZED		(451, "客户端未授权")
	,PROTO_ERR_SERVER_BUSY			(501, "服务器忙")
	,PROTO_ERR_SERVER_TIMEROUT		(502, "服务器超时(收盘?)")
	;
	private final int errorCode;
	private final String errorString;
	
	ErrorCode(int errorCode, String errorString)
	{
		this.errorCode = errorCode;
		this.errorString = errorString;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public String getErrorString() {
		return errorString;
	}
	
	

}