package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 上报下载进度（播放列表）（请求）
 */
public class Msg921000008 extends Msg921 implements Interface921000008{

    public String playlistUid;
    public float speed;
    public float percent;
    public List<File> fileList = new ArrayList<File>();
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000008.getCode();
    }
    @Override
    public void readData(ByteBuf buffer) {
        playlistUid = readString(buffer);
        speed = buffer.readFloat();
        percent = buffer.readFloat();
        int size = buffer.readInt();
        for(int i = 0; i < size; i++) {
            File file = new File();
            file.read(buffer);
            fileList.add(file);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, playlistUid);
        buffer.writeFloat(speed);
        buffer.writeFloat(percent);
        buffer.writeInt(fileList.size());
        for(File file: fileList){
            file.write(buffer);
        }
    }
}
