package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Msg081000003 extends Msg081 {

    static final Logger log = LogManager.getLogger(Msg081000003.class);

//    public static void main(String[] args) throws DecoderException {
//        String hex = "0101d905c5000000120000001f05d8ff383333594843196013303500dc00000000220000000000140a000000";
//        byte[] bytes = Hex.decodeHex(hex.toCharArray());
//        ByteBuf buf = Unpooled.buffer(100);
//        buf.writeBytes(bytes);
//
//        new Msg031000005().decode(buf);
//    }

    public String boxCode; //设备编号
    public String version; //设备版本
    public int seek; //
    public int length;


    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000003.getCode();
    }

    @Override
    public String getCode() {
        return boxCode;
    }

    @Override
    public void readData(ByteBuf buffer) {
        byte[] bytes = new byte[12];
        buffer.readBytes(bytes);

        boxCode = Hex.encodeHexString(bytes).toUpperCase();
        version = new String(new char[]{(char) buffer.readByte(), (char) buffer.readByte()});
        seek = buffer.readInt();
        length = buffer.readInt();
    }

}
