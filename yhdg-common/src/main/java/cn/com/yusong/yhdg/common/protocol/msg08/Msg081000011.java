package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

public class Msg081000011 extends Msg081 {
    public String checkSum;
    public Boolean checkCRC;

    public String code; //设备编号
    public String version; //设备版本

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000011.getCode();
    }

    @Override
    public String getCode() {
        return code;
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
        code = Hex.encodeHexString(bytes).toUpperCase();
        version = new String(new char[]{(char) buffer.readByte(), (char) buffer.readByte()});
    }
}
