package cn.com.yusong.yhdg.common.protocol.msg01;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;

public abstract class Msg012 extends Message {
    public short rtnCode;

    public void writeTime(ByteBuf buffer) {
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        buffer.writeInt(Integer.valueOf(timestamp));
    }

    protected void writeRtn(ByteBuf buffer) {
        buffer.writeShort(rtnCode);
    }

    protected void readRtn(ByteBuf buffer) {
        rtnCode = buffer.readShort();
    }

}
