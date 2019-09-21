package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

public class Msg081000009 extends Msg081 {
    public String checkSum;
    public Boolean checkCRC;

    public String code; //设备编号
    public String version; //设备版本

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000009.getCode();
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
        version = parseVersion(buffer.readByte());
    }


    public static String parseVersion(byte src) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[2];
        buffer[0] = Character.forDigit((src >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(src & 0x0F, 16);
        stringBuilder.append(buffer);
        String str = stringBuilder.toString();
        String returnVersion = "";
        if(str.length()> 0){
            returnVersion = str.substring(0,1) + "." + str.substring(1,str.length());
        }
        return returnVersion;
    }
}
