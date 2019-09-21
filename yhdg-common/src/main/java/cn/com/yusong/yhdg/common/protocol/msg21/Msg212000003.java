package cn.com.yusong.yhdg.common.protocol.msg21;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 有新菜鸟API消息
 */
public class Msg212000003 extends Msg212 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_212000003.getCode();
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
