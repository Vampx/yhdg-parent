package cn.com.yusong.yhdg.common.protocol.msg93;


import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg931000010 extends Msg931 {

    public String terminalId;
    public String address;
    public String tel;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_931000010.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, terminalId);
        writeString(buffer, address);
        writeString(buffer, tel);
    }

    @Override
    public void readData(ByteBuf buffer) {
        terminalId = readString(buffer);
        address = readString(buffer);
        tel = readString(buffer);
    }

}
