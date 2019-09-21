package cn.com.yusong.yhdg.common.protocol.msg06;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

/**
 * 电池长心跳
 */
public class Msg061000002 extends Msg061 {

    public int signType;
    public String sign;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_061000002.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        json = readString(buffer);
        signType = buffer.readByte();

        int length = buffer.readInt();
        byte[] buf = new byte[length];
        buffer.readBytes(buf);
        sign = new String(Hex.encodeHex(buf)).toUpperCase();
    }
}
