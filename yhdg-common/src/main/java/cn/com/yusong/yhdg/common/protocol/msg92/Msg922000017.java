package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 播放状态上报（响应）
 */
public class Msg922000017 extends Msg922  {

    public String uid;
    public String strategy;
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000017.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
        uid = readString(buffer);
        strategy = readString(buffer);

    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        writeString(buffer, uid);
        writeString(buffer, strategy);

    }
}
