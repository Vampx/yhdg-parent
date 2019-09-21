package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class Msg082 extends Message {
    public short rtnCode;

//    public String responseBodyHex;
//
//    public void setResponseBodyHex(ByteBuf buf){
//        buf.markReaderIndex();
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        responseBodyHex = new String(Hex.encodeHex(bytes));
//        buf.resetReaderIndex();
//    }

    protected void writeRtn(ByteBuf buffer) {
        buffer.writeShort(rtnCode);
    }

    protected void readRtn(ByteBuf buffer) {
        rtnCode = buffer.readShort();
    }

//    @Override
//    public String toString() {
//        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE).replace("responseBodyHex="+responseBodyHex, "").replaceAll("[ \r]","").replaceAll("\n\n","\n");
//    }
}
