package cn.com.yusong.yhdg.common.protocol.msg22;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg072;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

/**
 * 开门通知
 */
public class Msg222000004 extends Msg222 {

    public String batteryId;
    public byte boxStatus;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_222000004.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);

        byte[] bytes = new byte[4];
        buffer.readBytes(bytes);
        batteryId = new String(Hex.encodeHex(bytes)).toUpperCase();

        boxStatus=buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        try {
            byte[] bytes = Constant.EMPTY_BATTERY_BYTES;
            if(StringUtils.isNotEmpty(batteryId)) {
                bytes = Hex.decodeHex(batteryId.toCharArray());
            }
            buffer.writeBytes(bytes);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }

        buffer.writeByte(boxStatus);
    }
}
