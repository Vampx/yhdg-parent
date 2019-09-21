package cn.com.yusong.yhdg.common.protocol.msg07;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

/**
 * 开门通知
 */
public class Msg072000001 extends Msg072 {

    public String batteryId;
    public byte boxStatus;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_072000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);

        byte[] bytes = new byte[4];
        buffer.readBytes(bytes);
        batteryId = new String(Hex.encodeHex(bytes)).toUpperCase();
        boxStatus = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        try {
            byte[] bytes = Constant.EMPTY_BATTERY_BYTES;
            if (StringUtils.isNotEmpty(batteryId)) {
                bytes = Hex.decodeHex(batteryId.toCharArray());
            }
            buffer.writeBytes(bytes);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }

        buffer.writeByte(boxStatus);
    }
}
