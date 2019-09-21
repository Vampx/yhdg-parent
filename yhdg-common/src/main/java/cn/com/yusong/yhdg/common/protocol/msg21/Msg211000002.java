package cn.com.yusong.yhdg.common.protocol.msg21;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 发送心跳
 */
public class Msg211000002 extends Msg211 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_211000002.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
    }

    @Override
    public void writeData(ByteBuf buffer) {
    }
}
