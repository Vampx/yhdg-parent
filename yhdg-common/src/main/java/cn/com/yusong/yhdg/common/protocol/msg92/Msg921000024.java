package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 上报命令执行状态（请求）
 */
public class Msg921000024 extends Msg921{

    public int commandId;
    public byte status;
    public String failureReason;
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000024.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        commandId = buffer.readInt();
        status = buffer.readByte();
        failureReason = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeInt(commandId);
        buffer.writeByte(status);
        writeString(buffer, failureReason);
    }
}
