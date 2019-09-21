package cn.com.yusong.yhdg.common.protocol.msg_tbit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

public class TextMessageEncoder extends MessageToByteEncoder<TextMessage> {

    Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    protected void encode(ChannelHandlerContext ctx, TextMessage msg, ByteBuf out) throws Exception {
        String text = msg.encode();
        out.writeBytes(text.getBytes(UTF_8));
    }
}
