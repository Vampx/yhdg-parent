package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传终端日志
 */
public class Msg921000030 extends Msg921{
    public long logId;
    public String path;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000030.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeLong(logId);
        writeString(buffer, path);
    }
    @Override
    public void readData(ByteBuf buffer) {
        logId = buffer.readLong();
        path = readString(buffer);
    }
}
