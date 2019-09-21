package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg91.Msg911;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息上报（请求）
 */
public class Msg921000018 extends Msg921 implements Interface921000018{

    public List<Msg> msgList = new ArrayList<Msg>();
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000018.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        int size = buffer.readInt();
        for(int i = 0; i < size; i++) {
            Msg msg = new Msg();
            msg.read(buffer);
            msgList.add(msg);
        }

    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeInt(msgList.size());
        for(Msg msg : msgList) {
            msg.write(buffer);
        }
    }
}
