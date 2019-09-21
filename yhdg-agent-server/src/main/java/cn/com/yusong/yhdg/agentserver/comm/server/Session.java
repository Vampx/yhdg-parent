package cn.com.yusong.yhdg.agentserver.comm.server;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public abstract class Session {

    static final Logger log = LogManager.getLogger(Session.class);

    public static final String SESSION_KEY = "SESSION_KEY";

    ChannelHandlerContext handlerContext;
    Map<String, Object> attributes;

    public SessionId id;
    public volatile long heartTime;
    protected SessionManager sessionManager;

    public Session(SessionManager sessionManager, ChannelHandlerContext handlerContext, Map<String, Object> attributes, SessionId id) {
        this.sessionManager = sessionManager;
        this.handlerContext = handlerContext;
        this.attributes = attributes;
        this.id = id;
        this.heartTime = System.currentTimeMillis();
    }

    public void refreshHeartTime() {
        this.heartTime = System.currentTimeMillis();
    }

    public abstract void close();

    public ChannelFuture writeAndFlush(final Message message) {
        ChannelFuture future = handlerContext.writeAndFlush(message);
        if(log.isDebugEnabled()) {
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.debug("sent to {}, {}", handlerContext.channel().remoteAddress(), message);
                }
            });
        }
        return future;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
