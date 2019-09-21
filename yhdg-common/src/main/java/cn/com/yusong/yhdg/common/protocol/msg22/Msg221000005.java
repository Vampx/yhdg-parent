package cn.com.yusong.yhdg.common.protocol.msg22;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 开关门查询通知
 */
public class Msg221000005 extends Msg221 {

    public String cabinetId;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_221000005.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        cabinetId = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, cabinetId);
    }
}
