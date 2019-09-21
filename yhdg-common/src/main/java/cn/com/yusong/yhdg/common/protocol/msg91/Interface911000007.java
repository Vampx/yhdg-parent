package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

public interface Interface911000007 {

    public static class Property extends TypeOperator {
        public String name;
        public String value;

        public Property() {
        }

        public Property(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public void read(ByteBuf buffer) {
            name = readString(buffer);
            value = readString(buffer);
        }

        public void write(ByteBuf buffer) {
            writeString(buffer, name);
            writeString(buffer, value);
        }


    }
}
