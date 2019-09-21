package cn.com.yusong.yhdg.common.protocol.msg22;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 心跳
 */
public class Msg222000006 extends Msg222 {


    @Override
    public int getMsgCode() {
        return MsgCode.MSG_222000006.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
    }
}
