package cn.com.yusong.yhdg.common.protocol.msg07;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * 开关门查询通知
 */
public class Msg071000004 extends Msg071 {
    public String checkSum;
    public Boolean checkCRC;

    public String code;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_071000004.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        //Content所有字节异或校验
        byte[] bytes = new byte[2];
        buffer.readBytes(bytes);
        checkSum = new String(Hex.encodeHex(bytes)).toUpperCase();
        if(checkCRC(checkSum,  buffer)){
            checkCRC = true;
        }else{
            checkCRC = false;
        }

        bytes = new byte[12];
        buffer.readBytes(bytes);
        code = new String(Hex.encodeHex(bytes)).toUpperCase();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        try {
            buffer.writeBytes(Hex.decodeHex(code.toCharArray()));
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkCRC() {
        return true;
    }
}
