package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 上报离线列表内容（响应）
 */
public class Msg922000024 extends Msg922  {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000024.getCode();
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
