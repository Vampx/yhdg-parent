package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传崩溃日志（请求）
 */
public class Msg921000026 extends Msg921 implements Interface921000026{


    public List<File> fileList = new ArrayList<File>();
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000026.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {

        buffer.writeInt(fileList.size());
        for(File file : fileList) {
            file.write(buffer);
        }
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
}
