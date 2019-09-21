package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

public interface Interface921000008 {

    public static class File extends TypeOperator {
        public String path;
        public String name;
        public long length;
        public float percent;

        public File(String path, String name, long length, float percent) {
            this.path = path;
            this.name = name;
            this.length = length;
            this.percent = percent;
        }

        public File() {
        }

        public void write(ByteBuf buffer) {
            writeString(buffer, path);
            writeString(buffer, name);
            buffer.writeLong(length);
            buffer.writeFloat(percent);
        }
        public void read(ByteBuf buffer) {
            path = readString(buffer);
            name = readString(buffer);
            length = buffer.readLong();
            percent = buffer.readFloat();
        }

    }
}
