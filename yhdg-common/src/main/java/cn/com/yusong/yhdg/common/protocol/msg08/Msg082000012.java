package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg082000012 extends Msg082 {
    public int seek;
    public int length;
    public byte[] content;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_082000012.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        buffer.writeInt(seek);
        buffer.writeInt(length);
        for(byte e : content) {
            buffer.writeByte(e);
        }
    }

    @Override
    public boolean checkCRC() {
        return true;
    }
}
