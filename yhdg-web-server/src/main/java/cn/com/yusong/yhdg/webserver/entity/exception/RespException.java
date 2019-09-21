package cn.com.yusong.yhdg.webserver.entity.exception;

import cn.com.yusong.yhdg.webserver.constant.RespCode;

/**
 * 返回异常数据类
 * Created by chensy on 2017/6/10.
 */
public class RespException extends Exception {

    private int errorCode;//异常码

    private String errorMsg;//异常消息

    private Exception exception;

    /**
     * 私有构造请勿使用
     */
    private RespException() {
        super();
    }

    private RespException(int errorCode, String errorMsg,Exception e){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.exception = e;
    }

    public static RespException get(int errorCode, String errorMsg) {
        return get(errorCode,errorMsg,null);
    }

    public static RespException get(RespCode respCode) {
        return get(respCode.getValue(),respCode.getName(),null);
    }

    public static RespException get(int errorCode, String errorMsg, Exception e) {
        return new RespException(errorCode,errorMsg,e);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
