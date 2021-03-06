package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg91.Msg911;
import io.netty.buffer.ByteBuf;

/**
 * 查询播放列表版本（请求）
 */
public class Msg921000004 extends Msg921 {

    public int  playlistId;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000004.getCode();
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
