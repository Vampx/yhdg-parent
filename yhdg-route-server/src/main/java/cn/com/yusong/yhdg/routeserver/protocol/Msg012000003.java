package cn.com.yusong.yhdg.routeserver.protocol;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 查询业务服务器
 */
public class Msg012000003 extends ResponseMessage {
    String ip;
    int port;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_012000003.getCode();
    }

    @Override
    public void writeContent(Map<String, String> param) {
        param.put("Ip", StringUtils.trimToEmpty(ip));
        param.put("Port", String.format("%d", port));
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
