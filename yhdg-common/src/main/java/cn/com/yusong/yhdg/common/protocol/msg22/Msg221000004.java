package cn.com.yusong.yhdg.common.protocol.msg22;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg071;
import io.netty.buffer.ByteBuf;

/**
 * 开门通知
 */
public class Msg221000004 extends Msg221 {

    public String cabinetId;
    public byte lockNum; //锁号
    public byte boxType; //箱子类型

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_221000004.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        cabinetId = readString(buffer);
        lockNum = buffer.readByte();
        boxType = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, cabinetId);
        buffer.writeByte(lockNum);
        buffer.writeByte(boxType);
    }
}
