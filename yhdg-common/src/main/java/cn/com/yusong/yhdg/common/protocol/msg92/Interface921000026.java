package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

public interface Interface921000026 {

    public static class File extends TypeOperator {
        public String path;

        public File() {
        }

        public File(String path) {
            this.path = path;
        }

        public void write(ByteBuf buffer) {
            writeString(buffer, path);
        }
        public void read(ByteBuf buffer) {
            path = readString(buffer);
        }
    }
}
