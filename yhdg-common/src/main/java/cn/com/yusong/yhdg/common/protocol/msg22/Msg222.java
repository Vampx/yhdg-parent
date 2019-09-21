package cn.com.yusong.yhdg.common.protocol.msg22;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;

public abstract class Msg222 extends Message {

    public short rtnCode;
    public String rtnMsg;

    protected void writeRtn(ByteBuf buffer) {
        buffer.writeShort(rtnCode);
        writeString(buffer, rtnMsg);
    }

    protected void readRtn(ByteBuf buffer) {
        rtnCode = buffer.readShort();
        rtnMsg = readString(buffer);
    }
}
