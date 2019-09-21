package cn.com.yusong.yhdg.common.protocol.msg93;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg931000099 extends Msg931 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_931000099.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
    }

    @Override
    public void readData(ByteBuf buffer) {
    }

}
