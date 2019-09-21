package cn.com.yusong.yhdg.webserver.comm.client;

import cn.com.yusong.yhdg.common.protocol.msg21.Msg211000002;
import cn.com.yusong.yhdg.common.tool.netty.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 连接到 service-server的client
 */
public class ServiceServerClient extends NettyClient {
    BizFactory bizFactory;
    ExecutorService executorService;

    public ServiceServerClient(String host, int port, String threadName, BizFactory bizFactory, ExecutorService executorService) {
        super(host, port, 1, threadName);
        this.bizFactory = bizFactory;
        this.executorService = executorService;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new IdleStateHandler(0, 60, 0, TimeUnit.SECONDS));
        ch.pipeline().addLast(new MessageDecoder(MessageFactory.DEFAULT_INSTANCE));
        ch.pipeline().addLast(new MessageEncoder());
        ch.pipeline().addLast(new ServiceServerClientHandler(bizFactory, executorService));
    }

    private class ServiceServerClientHandler extends NettyHandler {

        public ServiceServerClientHandler(BizFactory bizFactory, ExecutorService executorService) {
            super(bizFactory, executorService);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                Msg211000002 msg = new Msg211000002();
                ctx.writeAndFlush(msg);
            }
        }
    }
}
