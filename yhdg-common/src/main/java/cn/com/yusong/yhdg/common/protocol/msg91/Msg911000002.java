package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 允许下载通知（请求）
 */
public class Msg911000002 extends Msg911 {

    public int  playlistId;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_911000002.getCode();
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
