package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg911000010 extends Msg911 {

    public String address;
    public String tel;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_911000010.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        address = readString(buffer);
        tel = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, address);
        writeString(buffer, tel);
    }
}
