package cn.com.yusong.yhdg.common.tool.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public abstract class Biz {
    Logger log = LogManager.getLogger(Biz.class);

    public abstract void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception;

    protected void writeAndFlush(final ChannelHandlerContext context, final Message message) {
        ChannelFuture future = context.writeAndFlush(message);
        if(log.isDebugEnabled()) {
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.debug("sent to {}, {}", context.channel().remoteAddress(), message);
                }
            });
        }
    }

    protected void writeAndClose(final ChannelHandlerContext context, final Message message) {
        ChannelFuture future = context.writeAndFlush(message);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(log.isDebugEnabled()) {
                    log.debug("sent to {}, {}", context.channel().remoteAddress(), message);
                }
                context.close();
                if(log.isDebugEnabled()) {
                    log.debug("invoke close()");
                }
            }
        });
    }

}
