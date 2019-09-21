package cn.com.yusong.yhdg.common.protocol.msg93;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 配置更新（响应）
 */
public class Msg932000008 extends Msg932  {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_932000008.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
    }
}
