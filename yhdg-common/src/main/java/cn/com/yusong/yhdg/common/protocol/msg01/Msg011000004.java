package cn.com.yusong.yhdg.common.protocol.msg01;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

/**
 * 查询业务服务器
 */
public class Msg011000004 extends Msg011 {

    public String json;
    public int signType;
    public String sign;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_011000004.getCode();
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
