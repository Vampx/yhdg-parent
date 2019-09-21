package cn.com.yusong.yhdg.frontserver.comm.session;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalOnlineService;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class TerminalSession extends Session {

    static final Logger log = LogManager.getLogger(TerminalSession.class);

    public int playlistId;

    public TerminalSession(ChannelHandlerContext handlerContext, Map<String, Object> attributes, String id) {
        super(handlerContext, attributes, id);
        this.handlerContext = handlerContext;
        this.attributes = attributes;
        attributes.put(SESSION_KEY, this);
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
