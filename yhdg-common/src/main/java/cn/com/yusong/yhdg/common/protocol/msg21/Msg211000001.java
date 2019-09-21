package cn.com.yusong.yhdg.common.protocol.msg21;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 发送短信
 */
public class Msg211000001 extends Msg211 {

    public int partnerId;
    public int moduleId;
    public byte sourceType;
    public String sourceId;
    public String mobile;
    public String content;
    public byte type;
    public String sign;
    public String variable;
    public String templateCode;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_211000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        partnerId = buffer.readInt();
        moduleId = buffer.readInt();
        sourceType = buffer.readByte();
        sourceId = readString(buffer);
        mobile = readString(buffer);
        content = readString(buffer);
        type = buffer.readByte();
        sign = readString(buffer);
        variable = readString(buffer);
        templateCode = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        buffer.writeInt(partnerId);
        buffer.writeInt(moduleId);
        buffer.writeByte(sourceType);
        writeString(buffer, sourceId);
        writeString(buffer, mobile);
        writeString(buffer, content);
        buffer.writeByte(type);
        writeString(buffer, sign);
        writeString(buffer, variable);
        writeString(buffer, templateCode);
    }
}
