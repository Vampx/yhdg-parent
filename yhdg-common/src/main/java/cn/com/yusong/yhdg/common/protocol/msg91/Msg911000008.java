package cn.com.yusong.yhdg.common.protocol.msg91;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 终端有新命令（请求）
 */
public class Msg911000008 extends Msg911{


    @Override
    public int getMsgCode() {
        return MsgCode.MSG_911000008.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {

    }

    @Override
    public void writeData(ByteBuf buffer) {

    }
}
