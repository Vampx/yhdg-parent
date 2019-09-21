package cn.com.yusong.yhdg.common.protocol.msg93;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 终端配置更新（请求）
 */
public class Msg931000007 extends Msg931 implements Interface931000007 {
    public String terminalId;
    public String terminalName;
    public String strategyUid;
    public byte logLevel;
    public String ftpEncoding;
    public String ftpUser;
    public String ftpPassword;
    public int ftpPort;
    public int playlistId;
    public String playlistName;
    public List<Property> propertyList = new ArrayList<Property>();

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_931000007.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        terminalId = readString(buffer);
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
        for(int i = 0; i < size; i++) {
            Property property = new Property();
            property.read(buffer);
            propertyList.add(property);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, terminalId);
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
        for(Property p : propertyList) {
            p.write(buffer);
        }
    }
}
