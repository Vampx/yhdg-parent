package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Msg082000011 extends Msg082 {
    public String md5;
    public int fileLength;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_082000011.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        byte[] md5Bytes;
        try {
            md5Bytes = Hex.decodeHex(md5.toCharArray());
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }

        buffer.writeByte(md5Bytes.length);
        buffer.writeBytes(md5Bytes);

        buffer.writeInt(fileLength);
    }

    @Override
    public boolean checkCRC() {
        return true;
    }
}
