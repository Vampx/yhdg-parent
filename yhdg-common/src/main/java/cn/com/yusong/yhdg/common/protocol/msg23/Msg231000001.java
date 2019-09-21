package cn.com.yusong.yhdg.common.protocol.msg23;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 给客户分配一个新满箱通知
 */
public class Msg231000001 extends Msg231 {

    public String cabinetId;
    public String boxNum;
    public long customerId;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_231000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        cabinetId = readString(buffer);
        boxNum = readString(buffer);
        customerId = buffer.readLong();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, cabinetId);
        writeString(buffer, boxNum);
        buffer.writeLong(customerId);
    }
}
