package cn.com.yusong.yhdg.webserver.comm.client;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_01;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.TextMessage;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.TextMessageEncoder;
import cn.com.yusong.yhdg.common.tool.netty.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 连接到 vehicle-server的client
 */
public class VehicleServerClient extends NettyClient {
    static final Logger log = LogManager.getLogger(VehicleServerClient.class);


    VehicleBizFactory bizFactory;
    ExecutorService executorService;

    public VehicleServerClient(String host, int port, String threadName, VehicleBizFactory bizFactory, ExecutorService executorService) {
        super(host, port, 1, threadName);
        this.bizFactory = bizFactory;
        this.executorService = executorService;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        final ByteBuf byteBuf = Unpooled.copiedBuffer("]".getBytes());

        ch.pipeline().addLast(new IdleStateHandler(0, 60, 0, TimeUnit.SECONDS));
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, byteBuf));
        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast(new TextMessageEncoder());
        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast(new VehicleServerClientHandler(bizFactory, executorService));
    }


    private class VehicleServerClientHandler extends SimpleChannelInboundHandler<String> {
        ExecutorService executorService = null;
        VehicleBizFactory bizFactory = null;
        protected Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

        public VehicleServerClientHandler ( VehicleBizFactory bizFactory , ExecutorService executorService) {
            this.bizFactory = bizFactory;
            this.executorService = executorService;
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, String text) throws Exception {

            if(log.isDebugEnabled()) {
                log.debug("this.instance: {}", this);
                log.debug("ChannelHandlerContext.instance: {}", ctx);
                log.debug("from:{}, recv:{}", ctx.channel().remoteAddress(), text);
            }

            TextMessage message = TextMessage.decode(text);
            if (message == null) {
                log.error("不可识别消息, text={}", text);
                return;
            }

            Biz biz = bizFactory.create(message.getMsgCode());
            if(biz != null) {
                MessageProcessor processor = new MessageProcessor();
                processor.channelHandlerContext = ctx;
                processor.message = message;
                processor.biz = biz;
                processor.attributes = attributes;
                executorService.submit(processor);
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                Msg_WEB_01 msg = new Msg_WEB_01();
                ctx.writeAndFlush(msg);
            }
        }
    }
}
