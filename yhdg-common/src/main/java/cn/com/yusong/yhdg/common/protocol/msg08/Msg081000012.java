package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Msg081000012 extends Msg081 {

    static final Logger log = LogManager.getLogger(Msg081000012.class);

    public String checkSum;
    public Boolean checkCRC;

    public String boxCode; //设备编号
    public String version; //设备版本
    public int seek; //
    public int length;


    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000012.getCode();
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
        version = new String(new char[]{(char) buffer.readByte(), (char) buffer.readByte()});
        seek = buffer.readInt();
        length = buffer.readInt();
    }

}
