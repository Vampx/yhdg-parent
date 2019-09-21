package cn.com.yusong.yhdg.vehicleserver.comm.server;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.TextMessageEncoder;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.netty.VehicleBizFactory;
import cn.com.yusong.yhdg.vehicleserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.vehicleserver.config.AppConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

public class MainServer {
    AppConfig config;

    Channel channel;
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public MainServer(AppConfig config) {
        this.config = config;
    }

    public void startup() throws InterruptedException {

        final ByteBuf byteBuf = Unpooled.copiedBuffer("]".getBytes());

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ReadTimeoutHandler(60 * 10));
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, byteBuf));
                        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(new TextMessageEncoder());
                        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(newNettyHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true);

        // Bind and start to accept incoming connections.
        ChannelFuture f = b.bind(config.getServerPort()).sync();
        channel = f.channel();
    }

    public void close() {
        channel.close();

        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    protected SimpleChannelInboundHandler<String> newNettyHandler() {
        return new MainServerHandler(new VehicleBizFactory(), config.serverExecutorService, SpringContextHolder.getBean(SessionManager.class));
    }
}
