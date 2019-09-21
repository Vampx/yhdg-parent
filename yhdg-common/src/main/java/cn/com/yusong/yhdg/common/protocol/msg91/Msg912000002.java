package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 允许下载通知（响应）
 */
public class Msg912000002 extends Msg912 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_912000002.getCode();
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
