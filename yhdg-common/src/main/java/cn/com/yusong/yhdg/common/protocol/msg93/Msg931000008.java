package cn.com.yusong.yhdg.common.protocol.msg93;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 终端有新命令（请求）
 */
public class Msg931000008 extends Msg931{
    public String terminalId;//

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_931000008.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        terminalId = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, terminalId);
    }
}
