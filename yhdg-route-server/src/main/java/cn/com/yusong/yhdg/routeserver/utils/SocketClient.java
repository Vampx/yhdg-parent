package cn.com.yusong.yhdg.routeserver.utils;

import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000004;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.netty.MessageDecoder;
import cn.com.yusong.yhdg.common.tool.netty.MessageEncoder;
import cn.com.yusong.yhdg.common.tool.netty.MessageFactory;
import cn.com.yusong.yhdg.routeserver.comm.server.MainServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


/**
 * Best Do It
 */
public class SocketClient {

    Channel channel;
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    public void startup() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MessageEncoder());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true);

        // Bind and start to accept incoming connections.
        ChannelFuture f = b.bind(12011).sync();
        channel = f.channel();
        channel.connect(new InetSocketAddress("127.0.0.1", 12010));
    }
    public static void main(String[] args) throws DecoderException {
        String hex = "04d3f644000000a1000001aa0668ff3633374b4d43122423313500060004000002310100000a01010000154a00000000000000000000000000000000000000000000000000000000000000000000020100000000000000000000000000000000000000000000000000000000000000000000000000000301000000000000000000000000000000000000000000000000000000000000000000000000000004010000000000000000000000000000000000000000000000000000000000000000000000000000050100000000000000000000000000000000000000000000000000000000000000000000000000000600000020cb5953444c3438323630303030303031149a00004e2000031000000b380b2e07ef00000701000000000000000000000000000000000000000000000000000000000000000000000000000008010000000000000000000000000000000000000000000000000000000000000000000000000000090100000000000000000000000000000000000000000000000000000000000000000000000000000a010000000000000000000000000000000000000000000000000000000000000000000000000000";
        ByteBuf buf = Unpooled.buffer(1024);
        buf.writeBytes(Hex.decodeHex(hex.toCharArray()));

        Msg081000004 msg = new Msg081000004();
        msg.decode(buf);
        msg.toParam();
    }
}