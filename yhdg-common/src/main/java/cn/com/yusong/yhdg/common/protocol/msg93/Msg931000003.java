package cn.com.yusong.yhdg.common.protocol.msg93;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 文件更新（播放列表）（请求）
 */
public class Msg931000003 extends Msg931{
    public int playlistId;
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_931000003.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        playlistId = buffer.readInt();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeInt(playlistId);
    }
}
