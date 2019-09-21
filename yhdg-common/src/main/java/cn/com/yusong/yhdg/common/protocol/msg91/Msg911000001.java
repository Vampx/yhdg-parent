package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 策略内容更新（请求）
 */
public class Msg911000001 extends Msg911 {

    public String uid;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_911000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        uid = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, uid);
    }
}
