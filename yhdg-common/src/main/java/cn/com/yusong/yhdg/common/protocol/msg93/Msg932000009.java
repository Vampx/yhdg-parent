package cn.com.yusong.yhdg.common.protocol.msg93;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg932000009 extends Msg932 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_932000009.getCode();
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
