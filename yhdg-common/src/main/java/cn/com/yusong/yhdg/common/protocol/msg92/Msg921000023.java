package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 上报离线列表内容（请求）
 */
public class Msg921000023 extends Msg921{


    public List<File> fileList = new ArrayList<File>();
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000023.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {

        int size = buffer.readInt();
        for(int i = 0; i < size; i++) {
            File file = new File();
            file.read(buffer);
            fileList.add(file);
        }

    }


    @Override
    public void writeData(ByteBuf buffer) {

        buffer.writeInt(fileList.size());
        for(File file : fileList) {
            file.write(buffer);
        }
    }
    public static class File extends TypeOperator {
        public String path;
        public String name;
        public long length;

        public void write(ByteBuf buffer) {
            writeString(buffer, path);
            writeString(buffer, name);
            buffer.writeLong(length);
        }
        public void read(ByteBuf buffer) {
            path = readString(buffer);
            name = readString(buffer);
            length = buffer.readLong();
        }

    }
}
