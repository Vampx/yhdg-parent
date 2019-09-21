package cn.com.yusong.yhdg.common.protocol.msg06;


import cn.com.yusong.yhdg.common.tool.netty.Message;
import io.netty.buffer.ByteBuf;

public abstract class Msg061 extends Message {
    public String json;
    public int time;

    public void readTime(ByteBuf buffer) {
        time = buffer.readInt();
    }
}
