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
public class Msg072000003 extends Msg072 {
    public String checkSum;
    public Boolean checkCRC;

    public String boxCode;
    public byte boxNum;
    public byte boxStatus;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_072000003.getCode();
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

        readRtn(buffer);
        bytes = new byte[12];
        buffer.readBytes(bytes);
        boxCode  = new String(Hex.encodeHex(bytes)).toUpperCase();
        boxNum = buffer.readByte();
        boxStatus = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
    }
}
