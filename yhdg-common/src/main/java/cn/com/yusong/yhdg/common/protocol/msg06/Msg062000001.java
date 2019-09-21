package cn.com.yusong.yhdg.common.protocol.msg06;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg01.Msg012000004;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.security.MessageDigest;

/**
 * 电池心跳
 */
public class Msg062000001 extends Msg062 {
    public String json;
    public String sign;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_062000001.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        writeString(buffer, json);

        //加签名
        buffer.writeByte(1);//加密规则 md5
        byte[] signByte = CodecUtils.signMd5ForByte(json);
        buffer.writeInt(signByte.length);
        buffer.writeBytes(signByte);
        sign = Hex.encodeHexString(signByte).toUpperCase();
    }

}
