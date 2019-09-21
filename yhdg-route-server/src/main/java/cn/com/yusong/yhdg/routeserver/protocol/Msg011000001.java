package cn.com.yusong.yhdg.routeserver.protocol;

import javax.servlet.http.HttpServletRequest;

/**
 * 查询业务服务器
 */
public class Msg011000001 extends RequestMessage {

    String code;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_011000001.getCode();
    }

    @Override
    public void readContent(HttpServletRequest request) {
        code = request.getParameter("Code");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
