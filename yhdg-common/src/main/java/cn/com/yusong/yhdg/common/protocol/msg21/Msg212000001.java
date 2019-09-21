package cn.com.yusong.yhdg.common.protocol.msg21;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 发送短信
 */
public class Msg212000001 extends Msg212 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_212000001.getCode();
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
