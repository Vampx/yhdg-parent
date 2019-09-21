package cn.com.yusong.yhdg.common.protocol.msg06;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

/**
 * 固件下载
 */
public class Msg062000003 extends Msg062 {
    public int seek;
    public int length;
    public byte[] content;

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_062000003.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        buffer.writeInt(seek);
        buffer.writeInt(length);
/*        for(byte e : content) {
            buffer.writeByte(e);
        }*/
        buffer.writeBytes(content);
    }

}
