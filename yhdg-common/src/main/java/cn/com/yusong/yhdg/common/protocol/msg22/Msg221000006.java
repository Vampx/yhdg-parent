package cn.com.yusong.yhdg.common.protocol.msg22;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 心跳
 */
public class Msg221000006 extends Msg221 {

    public static final byte CLIENT_TYPE_STATIC_SERVER = 1;

    public byte clientType;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_221000006.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        clientType = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeByte(clientType);
    }
}
