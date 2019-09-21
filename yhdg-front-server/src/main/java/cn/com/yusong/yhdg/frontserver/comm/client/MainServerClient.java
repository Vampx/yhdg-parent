package cn.com.yusong.yhdg.frontserver.comm.client;

import cn.com.yusong.yhdg.common.tool.netty.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class MainServerClient extends NettyClient {

    static Logger log = LogManager.getLogger(MainServerClient.class);


    BizFactory bizFactory;
    ExecutorService executorService;

    public MainServerClient(String host, int port, String threadName, BizFactory bizFactory, ExecutorService executorService, Notice offlineNotice, Notice connectedNotice) {
        super(host, port, 1, threadName, offlineNotice, connectedNotice);
        this.bizFactory = bizFactory;
        this.executorService = executorService;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new MessageDecoder(MessageFactory.DEFAULT_INSTANCE));
        ch.pipeline().addLast(new MessageEncoder());
        ch.pipeline().addLast(new TerminalServerClientHandler(bizFactory, executorService));
    }

    private class TerminalServerClientHandler extends NettyHandler {

        public TerminalServerClientHandler(BizFactory bizFactory, ExecutorService executorService) {
            super(bizFactory, executorService);
        }
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
            }
        }
    }

    public ChannelFuture writeAndFlush(Object object) {
        if(getChannel() == null) {
            log.warn("getChannel() is null");
            return null;
        }

        if(log.isDebugEnabled()) {
            log.debug("sent msg {}", object);
        }
        return getChannel().writeAndFlush(object);
    }
}
