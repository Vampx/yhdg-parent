package cn.com.yusong.yhdg.routeserver.protocol;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 查询业务服务器
 */
public class Msg011000003 extends RequestMessage {

    String code;
    String ccid;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_011000003.getCode();
    }

    @Override
    public void readContent(HttpServletRequest request) {
        code = StringUtils.trimToEmpty(request.getParameter("Code"));
        ccid = request.getParameter("Ccid");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }
}
