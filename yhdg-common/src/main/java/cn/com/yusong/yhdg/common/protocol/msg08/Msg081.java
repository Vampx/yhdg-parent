package cn.com.yusong.yhdg.common.protocol.msg08;


import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class Msg081 extends Message {

//    public String requestBodyHex;
//
//    public void setRequestBodyHex(ByteBuf buf){
//        buf.markReaderIndex();
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        requestBodyHex = new String(Hex.encodeHex(bytes));
//        buf.resetReaderIndex();
//    }

    public abstract String getCode();

//    @Override
//    public String toString() {
//        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE).replace("requestBodyHex="+requestBodyHex, "").replaceAll("[ \r]","").replaceAll("\n\n","\n");
//    }
}
