package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 终端心跳（请求）
 */
public class Msg921000003 extends Msg921{
    public float cpu;
    public float memory;
    public long cardCapacity;
    public long restCapacity;
    public byte signal;
    public byte playVolume;
    public byte playMode;
    public String strategyUid;
    public String playlistUid;
    public byte downloadStatus;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000003.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        cpu = buffer.readFloat();
        memory = buffer.readFloat();
        cardCapacity = buffer.readLong();
        restCapacity = buffer.readLong();
        signal = buffer.readByte();
        playVolume = buffer.readByte();
        playMode = buffer.readByte();
        strategyUid = readString(buffer);
        playlistUid = readString(buffer);
        downloadStatus = buffer.readByte();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeFloat(cpu);
        buffer.writeFloat(memory);
        buffer.writeLong(cardCapacity);
        buffer.writeLong(restCapacity);
        buffer.writeByte(signal);
        buffer.writeByte(playVolume);
        buffer.writeByte(playMode);
        writeString(buffer, strategyUid);
        writeString(buffer, playlistUid);
        buffer.writeByte(downloadStatus);
    }
}
