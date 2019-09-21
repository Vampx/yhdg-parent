package cn.com.yusong.yhdg.routeserver.config;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ServerInfo {
    public String version;
    public String ip;
    public int port;
    public int weight;

    public ServerInfo(String version, String ip, int port, int weight) {
        this.version = version;
        this.ip = ip;
        this.port = port;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
