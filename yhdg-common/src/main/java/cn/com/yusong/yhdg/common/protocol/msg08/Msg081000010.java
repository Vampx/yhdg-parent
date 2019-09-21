package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Msg081000010 extends Msg081 {

    static final Logger log = LogManager.getLogger(Msg081000010.class);

    public String checkSum;
    public Boolean checkCRC;

    public String boxCode; //设备编号
    public String version; //设备版本
    public int seek; //
    public int length;


    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000010.getCode();
    }

    @Override
    public String getCode() {
        return boxCode;
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

        boxCode = Hex.encodeHexString(bytes).toUpperCase();
        version = parseVersion(buffer.readByte());
        seek = buffer.readInt();
        length = buffer.readInt();
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
