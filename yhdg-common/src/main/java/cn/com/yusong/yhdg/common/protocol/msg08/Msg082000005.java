package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

public class Msg082000005 extends Msg082 {
    //
    public byte checksum;

    public byte cabinetType;
    public byte activeFanTemp;
    public byte hasNewVersion;
    public byte hasNewSubVersion;
    public byte fullVolume;
    public int power;
    public int boxMaxPower;
    public int boxMinPower;
    public int trickleTime;
    public short reserved;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_082000005.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        //
        buffer.writeByte(checksum);

        buffer.writeByte(cabinetType);
        buffer.writeByte(activeFanTemp);
        buffer.writeByte(hasNewVersion);
        buffer.writeByte(hasNewSubVersion);
        buffer.writeByte(fullVolume);
        buffer.writeShort(power);
        buffer.writeShort(boxMaxPower);
        buffer.writeShort(boxMinPower);
        buffer.writeShort(trickleTime);
        buffer.writeShort(reserved);
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
        cabinetType = buffer.readByte();
        activeFanTemp = buffer.readByte();
        hasNewVersion = buffer.readByte();
        hasNewSubVersion = buffer.readByte();
        fullVolume = buffer.readByte();
        power = buffer.readShort();
        boxMaxPower = buffer.readShort();
        boxMinPower = buffer.readShort();
        trickleTime = buffer.readShort();
        reserved = buffer.readShort();
    }

}
