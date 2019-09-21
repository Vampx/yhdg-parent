package cn.com.yusong.yhdg.common.protocol.msg07;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 开门通知
 */
public class Msg071000001 extends Msg071 {

    public byte lockNum; //锁号
    public byte boxType; //箱子类型

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_071000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        lockNum = buffer.readByte();
        boxType = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeByte(lockNum);
        buffer.writeByte(boxType);
    }
}
