package cn.com.yusong.yhdg.agentserver.comm.client;

import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000099;
import cn.com.yusong.yhdg.common.tool.netty.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 连接到 Front-server的client
 */
public class FrontServerClient extends NettyClient {
    BizFactory bizFactory;
    ExecutorService executorService;

    public FrontServerClient(String host, int port, String threadName, BizFactory bizFactory, ExecutorService executorService) {
        super(host, port, 1, threadName);
        this.bizFactory = bizFactory;
        this.executorService = executorService;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new IdleStateHandler(0, 60, 0, TimeUnit.SECONDS));
        ch.pipeline().addLast(new MessageDecoder(MessageFactory.DEFAULT_INSTANCE));
        ch.pipeline().addLast(new MessageEncoder());
        ch.pipeline().addLast(new FrontServerClientHandler(bizFactory, executorService));
    }

    private class FrontServerClientHandler extends NettyHandler {

        public FrontServerClientHandler(BizFactory bizFactory, ExecutorService executorService) {
            super(bizFactory, executorService);
        }
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                Msg931000099 msg = new Msg931000099();
                ctx.writeAndFlush(msg);
            }
        }
    }
}
