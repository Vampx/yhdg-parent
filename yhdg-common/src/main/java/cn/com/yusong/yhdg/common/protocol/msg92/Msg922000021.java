package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 请求下载（响应）
 */
public class Msg922000021 extends Msg922  {
    public byte download;//是否允许下载 1 允许 0 不允许

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000021.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
        download = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        buffer.writeByte(download);
    }
}
