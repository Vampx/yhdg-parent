package cn.com.yusong.yhdg.common.tool.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageEncoder extends MessageToByteEncoder<Message> {

    static final Logger log = LogManager.getLogger(MessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        message.encode(byteBuf);

        if(log.isDebugEnabled()) {
            byteBuf.markReaderIndex();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            log.debug("sent {} to hex: {}", channelHandlerContext.channel().remoteAddress(), new String(Hex.encodeHex(bytes)));

            byteBuf.resetReaderIndex();
        }
    }
}
