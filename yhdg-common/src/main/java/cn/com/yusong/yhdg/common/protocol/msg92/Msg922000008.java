package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 上报下载进度（播放列表）（响应）
 */
public class Msg922000008 extends Msg922  {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000008.getCode();
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
