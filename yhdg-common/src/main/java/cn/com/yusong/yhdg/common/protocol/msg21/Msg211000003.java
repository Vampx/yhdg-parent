package cn.com.yusong.yhdg.common.protocol.msg21;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 有新菜鸟API消息
 */
public class Msg211000003 extends Msg211 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_211000003.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
    }

    @Override
    public void writeData(ByteBuf buffer) {
    }
}
