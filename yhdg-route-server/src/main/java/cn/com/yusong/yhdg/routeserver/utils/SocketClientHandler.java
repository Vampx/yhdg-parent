package cn.com.yusong.yhdg.routeserver.utils;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.codec.binary.Hex;

import java.util.concurrent.TimeUnit;

public class SocketClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
//            Response resp = (Response)msg;
//            System.out.println("Client : " + resp.getId() + ", " + resp.getName() + ", " + resp.getResponseMessage());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public static void main(String[] args) throws Exception{
        SocketClient socketClient=new SocketClient();
        socketClient.startup();
        Channel channel=socketClient.channel;

        String hex = "04d3f64400000005000001aa066efA3131414b4d43181939313700170019000140890100000a01010000000000000000000000000000000000000000000000000000000000000000000000000000020100000000000000000000000000000000000000000000000000000000000b0000000000000000030100000000000000000000000000000000000000000000000000000000000000000000000000000401000000000000000000000000000000000000000000000000000000000000000000000000000005010000000000000000000000000000000000000000000000000000000000000000000000000000060100000000000000000000000000000000000000000000000000000000000000000000000000000701000000000000000000000000000000000000000000000000000000000000000000000000000008010000000000000000000000000000000000000000000000000000000000000000000000000000090100000000000000000000000000000000000000000000000000000000000000000000000000000a010000000000000000000000000000000000000000000000000000000000050000000000000000";
        ByteBuf buf = Unpooled.buffer(1024);
        buf.writeBytes(Hex.decodeHex(hex.toCharArray()));
        channel.write(buf.nioBuffer());

        channel.closeFuture().sync();


        System.out.println("断开连接,主线程结束..");

    }

}

