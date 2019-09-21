package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public interface Interface922000012 {

    public static class Detail  extends TypeOperator  {
        public String name;
        public String beginTime;
        public String endTime;
        public List<File> fileList = new ArrayList<File>();

        public void read(ByteBuf buffer) {
            name = readString(buffer);
            beginTime = readString(buffer);
            endTime = readString(buffer);
            int size = buffer.readInt();
            for(int i = 0; i < size; i++) {
                File file = new File();
                file.read(buffer);
                fileList.add(file);
            }
        }
        public void write(ByteBuf buffer) {
            writeString(buffer, name);
            writeString(buffer, beginTime);
            writeString(buffer, endTime);
            buffer.writeInt(fileList.size());
            for(File file : fileList) {
                file.write(buffer);
            }
        }

    }

    public static class File  extends TypeOperator  {
        public String path;
        public String name;
        public Long length;
        public String md5;
        public int duration;
        public void write(ByteBuf buffer) {
            writeString(buffer, path);
            writeString(buffer, name);
            buffer.writeLong(length);
            writeString(buffer, md5);
            buffer.writeInt(duration);
        }
        public void read(ByteBuf buffer) {
            path = readString(buffer);
            name = readString(buffer);
            length = buffer.readLong();
            md5 = readString(buffer);
            duration = buffer.readInt();
        }
    }
}
