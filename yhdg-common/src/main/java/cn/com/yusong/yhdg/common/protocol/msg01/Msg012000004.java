package cn.com.yusong.yhdg.common.protocol.msg01;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

/**
 * 查询业务服务器
 */
public class Msg012000004 extends Msg012 {
    public Json json = new Json();
    public String sign;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_012000004.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        String jsonStr = null;
        try {
            jsonStr = YhdgUtils.encodeJson(json)
                    .replaceAll("\\r","")
                    .replaceAll("\\n","")
                    .replaceAll(" ","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeString(buffer, jsonStr);

        //加签名
        buffer.writeByte(1);//加密规则 md5
        byte[] signByte = CodecUtils.signMd5ForByte(jsonStr);
        buffer.writeInt(signByte.length);
        buffer.writeBytes(signByte);
        sign = Hex.encodeHexString(signByte).toUpperCase();
    }

    public static class Json extends TypeOperator {
        public String ip;
        public int port;
    }

}
