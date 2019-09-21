package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询播放列表版本（响应）
 */
public class Msg922000004 extends Msg922  {

    public String playlistUid;//播放列表版本uid
    public byte exists;//文件在服务器上是否存在 1 存在 0 不存在
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000004.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
        playlistUid = readString(buffer);
        exists = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        writeString(buffer, playlistUid);
        buffer.writeByte(exists);
    }
}
