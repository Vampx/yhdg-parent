package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 上传崩溃日志（响应）
 */
public class Msg922000030 extends Msg922  {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000030.getCode();
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
