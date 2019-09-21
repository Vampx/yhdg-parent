package cn.com.yusong.yhdg.routeserver.protocol;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestMessage {

    public abstract int getMsgCode();
    public abstract void readContent(HttpServletRequest request);

    public void decode(HttpServletRequest request) {
        readContent(request);
    }
}
