package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

public interface Interface921000015 {

    public static class Area extends TypeOperator {
        public int id;//区域id
        public byte type;//类型 1 播放区域 2 字幕区域
        public String name;//素材名称

        public void write(ByteBuf buffer) {
            buffer.writeInt(id);
            buffer.writeByte(type);
            writeString(buffer, name);
        }
        public void read(ByteBuf buffer) {
            id = buffer.readInt();
            type = buffer.readByte();
            name = readString(buffer);
        }

    }
}
