package cn.com.yusong.yhdg.common.protocol.msg07;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * 开门通知
 */
public class Msg071000003 extends Msg071 {
    public String checkSum;
    public Boolean checkCRC;

    public String code;
    public byte lockNum; //锁号
    public byte boxType; //箱子类型
    public byte openType; //开门类型

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_071000003.getCode();
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
        lockNum = buffer.readByte();
        boxType = buffer.readByte();
        openType = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {

        try {
            buffer.writeBytes(Hex.decodeHex(code.toCharArray()));
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        buffer.writeByte(lockNum);
        buffer.writeByte(boxType);
        buffer.writeByte(openType);
    }

    @Override
    public boolean checkCRC() {
        return true;
    }
}
