package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放状态上报（请求）
 */
public class Msg921000015 extends Msg921 implements Interface921000015 {

    public byte  playMode;//1 列表 2 插播 2 离线
    public String playlistUid;
    public String playlistName;
    public int detailId;
    public String detailName;
    public int templateId;
    public String templateName;
    public String materialName;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000015.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        playMode = buffer.readByte();
        playlistUid = readString(buffer);
        playlistName = readString(buffer);
        detailId = buffer.readInt();
        detailName = readString(buffer);
        templateId = buffer.readInt();
        templateName = readString(buffer);
        materialName = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeByte(playMode);
        writeString(buffer, playlistUid);
        writeString(buffer, playlistName);
        buffer.writeInt(detailId);
        writeString(buffer, detailName);
        buffer.writeInt(templateId);
        writeString(buffer, templateName);
        writeString(buffer, materialName);
    }
}
