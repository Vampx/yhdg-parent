package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public interface Interface921000018 {

    public static class Msg extends TypeOperator {
        public long time;
        public byte level;
        public String tag;
        public String content;

        public void write(ByteBuf buffer) {
            buffer.writeLong(time);
            buffer.writeByte(level);
            writeString(buffer, tag);
            writeString(buffer, content);
        }

        public void read(ByteBuf buffer) {
            time = buffer.readLong();
            level = buffer.readByte();
            tag = readString(buffer);
            content = readString(buffer);
        }

    }
}
