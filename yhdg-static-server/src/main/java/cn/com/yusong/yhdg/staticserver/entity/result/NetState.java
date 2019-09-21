package cn.com.yusong.yhdg.staticserver.entity.result;

/**
 * 网络状态
 */
public class NetState {
	public static final int SUCCESS = 200;// 链接成功
	public static final int DATA_NOT_FOUND = 300;// 数据库未找到数据
	public static final int PARAMS_ERROR = 400;// 客户端参数错误
	public static final int SERVER_ERROR = 500;// 服务端异常
	private String data;// 网络状态
	private int errorCode;// 网络状态码

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
