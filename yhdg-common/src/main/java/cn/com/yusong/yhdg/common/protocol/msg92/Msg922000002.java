package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 终端登陆（响应）
 */
public class Msg922000002 extends Msg922 implements Interface922000002 {
    public String id;//终端名称
    public String terminalName;//终端名称
    public String strategyUid;//终端策略
    public byte logLevel;//日志级别
    public String ftpEncoding;
    public String ftpUser;
    public String ftpPassword;
    public int ftpPort;
    public int playlistId;
    public String playlistName;
    public List<Property> propertyList = new ArrayList<Property>();

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000002.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
        id = readString(buffer);
        terminalName = readString(buffer);
        strategyUid = readString(buffer);
        logLevel = buffer.readByte();
        ftpEncoding = readString(buffer);
        ftpUser = readString(buffer);
        ftpPassword = readString(buffer);
        ftpPort = buffer.readInt();
        playlistId = buffer.readInt();
        playlistName = readString(buffer);
        int size = buffer.readInt();

        for(int i = 0; i < size; i++){
            Property property = new Property();
            property.read(buffer);
            propertyList.add(property);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        writeString(buffer, id);
        writeString(buffer, terminalName);
        writeString(buffer, strategyUid);
        buffer.writeByte(logLevel);
        writeString(buffer, ftpEncoding);
        writeString(buffer, ftpUser);
        writeString(buffer, ftpPassword);
        buffer.writeInt(ftpPort);
        buffer.writeInt(playlistId);
        writeString(buffer, playlistName);
        buffer.writeInt(propertyList.size());
        for(Property p:propertyList){
            p.write(buffer);
        }
    }
}
