package cn.com.yusong.yhdg.common.protocol.msg07;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;

public abstract class Msg072 extends Message {

    public short rtnCode;

    protected void writeRtn(ByteBuf buffer) {
        buffer.writeShort(rtnCode);
    }

    protected void readRtn(ByteBuf buffer) {
        rtnCode = buffer.readShort();
    }
}
