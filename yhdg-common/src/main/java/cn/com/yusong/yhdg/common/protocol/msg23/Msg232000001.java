package cn.com.yusong.yhdg.common.protocol.msg23;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 给客户分配一个新满箱通知
 */
public class Msg232000001 extends Msg232 {

    public String cabinetId;
    public String subcabinetId;
    public String boxNum;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_232000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);

        cabinetId = readString(buffer);
        subcabinetId = readString(buffer);
        boxNum = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        writeString(buffer, cabinetId);
        writeString(buffer, subcabinetId);
        writeString(buffer, boxNum);
    }
}
