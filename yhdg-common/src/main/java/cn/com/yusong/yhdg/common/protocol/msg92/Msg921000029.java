package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传播放日志（请求）
 */
public class Msg921000029 extends Msg921 {

    public String path;
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000029.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, path);
    }
    @Override
    public void readData(ByteBuf buffer) {
        path = readString(buffer);
    }
}
