package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 请求下载（请求）
 */
public class Msg921000021 extends Msg921 {

    public int  playlistId;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000021.getCode();
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
