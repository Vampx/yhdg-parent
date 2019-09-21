package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

public interface Interface922000025 {

    public static class Command extends TypeOperator {
        public int id;
        public byte type;
        public String content;

        public Command() {
        }

        public Command(int id, byte type, String content) {
            this.id = id;
            this.type = type;
            this.content = content;
        }

        public void write(ByteBuf buffer) {
            buffer.writeInt(id);
            buffer.writeByte(type);
            writeString(buffer, content);
        }
        public void read(ByteBuf buffer) {
            id = buffer.readInt();
            type = buffer.readByte();
            content = readString(buffer);
        }

    }
}
