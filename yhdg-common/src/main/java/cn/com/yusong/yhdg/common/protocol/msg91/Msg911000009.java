package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg911000009 extends Msg911 {

    public long logId;
    public int type;
    public String logTime;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_911000009.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeLong(logId);
        buffer.writeInt(type);
        writeString(buffer, logTime);
    }
}
