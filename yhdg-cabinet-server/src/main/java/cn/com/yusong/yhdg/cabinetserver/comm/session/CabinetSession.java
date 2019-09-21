package cn.com.yusong.yhdg.cabinetserver.comm.session;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CabinetSession extends Session {

    static final Logger log = LogManager.getLogger(CabinetSession.class);

    public static final String TERMINAL_SESSION_KEY = "SESSION_KEY";
    public static final int NORMAL_CABINET_HERT = 1;
    public static final int NEW_CABINET_HERT = 2;
    public int heartBizType = NORMAL_CABINET_HERT;//1 旧心跳协议 2 新心跳协议
    public boolean reconnect = false;


    public CabinetSession(ChannelHandlerContext handlerContext, Map<String, Object> attributes, String id) {
        super(handlerContext, attributes, id);

        this.handlerContext = handlerContext;
        this.attributes = attributes;
        long time= System.currentTimeMillis();
        attributes.put(TERMINAL_SESSION_KEY, this);
    }

    public CabinetSession(ChannelHandlerContext handlerContext, Map<String, Object> attributes, String id, int heartBizType) {
        super(handlerContext, attributes, id);

        this.handlerContext = handlerContext;
        this.attributes = attributes;
        this.heartBizType = heartBizType;
        long time= System.currentTimeMillis();
        this.reconnect = true;
        attributes.put(TERMINAL_SESSION_KEY, this);
    }

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
