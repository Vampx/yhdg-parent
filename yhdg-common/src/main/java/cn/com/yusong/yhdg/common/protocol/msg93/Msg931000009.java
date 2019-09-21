package cn.com.yusong.yhdg.common.protocol.msg93;


import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg931000009 extends Msg931 {

    public String terminalId;
    public long logId;
    public int type;
    public String logTime;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_931000009.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, terminalId);
        buffer.writeLong(logId);
        buffer.writeInt(type);
        writeString(buffer, logTime);
    }

    @Override
    public void readData(ByteBuf buffer) {
        terminalId = readString(buffer);
        logId = buffer.readLong();
        type = buffer.readInt();
        logTime = readString(buffer);
    }

}
