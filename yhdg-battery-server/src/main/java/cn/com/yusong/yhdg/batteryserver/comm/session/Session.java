package cn.com.yusong.yhdg.batteryserver.comm.session;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Session {

    static final Logger log = LogManager.getLogger(Session.class);

    public static final String SESSION_KEY = "SESSION_KEY";

    public ChannelHandlerContext handlerContext;
    Map<String, Object> attributes;

    public String id;
    public volatile long heartTime = System.currentTimeMillis();

    public Session(ChannelHandlerContext handlerContext, Map<String, Object> attributes, String id) {
        this.handlerContext = handlerContext;
        this.attributes = attributes;
        this.id = id;
    }

    public void refreshHeartTime() {
        this.heartTime = System.currentTimeMillis();
    }

    public void close() {
        handlerContext.close();
        attributes.remove(SESSION_KEY);
        if(log.isDebugEnabled()) {
            log.debug("invoke close()");
        }
    }

    public ChannelFuture writeAndFlush(Message message) {
        return handlerContext.writeAndFlush(message);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
