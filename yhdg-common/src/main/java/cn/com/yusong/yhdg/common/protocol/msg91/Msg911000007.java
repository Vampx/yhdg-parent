package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg93.Interface931000007;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;


/**
 * 终端配置更新（请求）
 */
public class Msg911000007 extends Msg911 implements Interface931000007 {
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
        return MsgCode.MSG_911000007.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {

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
