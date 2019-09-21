package cn.com.yusong.yhdg.common.protocol.msg01;


import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;

public abstract class Msg011 extends Message {
    public int time;

    public void readTime(ByteBuf buffer) {
        time = buffer.readInt();
    }
}
