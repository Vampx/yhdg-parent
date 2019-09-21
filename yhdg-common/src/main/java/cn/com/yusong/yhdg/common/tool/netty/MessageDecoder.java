package cn.com.yusong.yhdg.common.tool.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

    static final Logger log = LogManager.getLogger(MessageDecoder.class);

    MessageFactory messageFactory;

    public MessageDecoder(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buf, List<Object> list) throws Exception {

        if(log.isDebugEnabled()) {
            buf.markReaderIndex();
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            log.debug("from {} recv hex: {}", context.channel().remoteAddress(), new String(Hex.encodeHex(bytes)));

            buf.resetReaderIndex();
        }

        buf.markReaderIndex();
        if(buf.readableBytes() >= 12) {
            int msgCode = buf.readInt(); //msgCode

            if(msgCode / 1000000 == 11 || msgCode / 1000000 == 61 ){
                if(buf.readableBytes() >= 12){
                     buf.readInt();
                }else{
                    buf.resetReaderIndex();
                    return;
                }
            }

            buf.readInt(); //searial number

            int length = buf.readInt();
            if(buf.readableBytes() >= length) { // ok
                Class<? extends Message> clazz = messageFactory.getClass(msgCode);
                if(clazz == null) {
                    if(log.isWarnEnabled()) {
                        log.warn("Not recognition message code: {}", msgCode);
                    }
                } else {
                    buf.resetReaderIndex();
                    Message message = clazz.newInstance();
                    message.decode(buf);
                    list.add(message);
                }

            } else {
                buf.resetReaderIndex();
            }
        } else {
            buf.resetReaderIndex();
        }
    }
}
