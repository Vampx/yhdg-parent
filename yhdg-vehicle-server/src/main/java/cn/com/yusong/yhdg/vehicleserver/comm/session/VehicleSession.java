package cn.com.yusong.yhdg.vehicleserver.comm.session;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.TextMessage;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class VehicleSession extends Session {

    static final Logger log = LogManager.getLogger(VehicleSession.class);

    public static final String TERMINAL_SESSION_KEY = "SESSION_KEY";

    public VehicleSession(ChannelHandlerContext handlerContext, Map<String, Object> attributes, String id) {
        super(handlerContext, attributes, id);

        this.handlerContext = handlerContext;
        this.attributes = attributes;
        attributes.put(TERMINAL_SESSION_KEY, this);
    }

    public ChannelFuture writeAndFlush(final TextMessage message) {
        ChannelFuture future = handlerContext.writeAndFlush(message);
        if(log.isDebugEnabled()) {
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.debug("sent to {}, {}, {}", handlerContext.channel().remoteAddress(), message, message.encode());
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
