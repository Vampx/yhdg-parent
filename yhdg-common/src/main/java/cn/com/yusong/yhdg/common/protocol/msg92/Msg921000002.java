package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 终端登陆（请求）
 */
public class Msg921000002 extends Msg921 {
    public String uid;//终端名称
    public String version;//终端策略
    public String code;
    public String ip;
    public String validateCode;//md5(uid + version + code + ip + timestamp)

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000002.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        uid = readString(buffer);
        version = readString(buffer);
        code = readString(buffer);
        ip = readString(buffer);
        validateCode = readString(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeString(buffer, uid);
        writeString(buffer, version);
        writeString(buffer, code);
        writeString(buffer, ip);
        writeString(buffer, validateCode);
    }
}
