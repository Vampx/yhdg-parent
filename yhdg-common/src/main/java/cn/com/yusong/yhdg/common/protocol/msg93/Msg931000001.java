package cn.com.yusong.yhdg.common.protocol.msg93;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 策略内容更新（请求）
 */
public class Msg931000001 extends Msg931{
    public String terminalId;
    public String uid;//策略UID(id-version)

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_931000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        terminalId = readString(buffer);
        uid = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, terminalId);
        writeString(buffer, uid);
    }
}
