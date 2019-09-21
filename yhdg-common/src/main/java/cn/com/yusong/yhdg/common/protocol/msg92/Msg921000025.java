package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg91.Msg911;
import io.netty.buffer.ByteBuf;

/**
 * 查询未执行命令（请求）
 */
public class Msg921000025 extends Msg921{


    @Override
    public int getMsgCode() {
        return MsgCode.MSG_921000025.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {

    }

    @Override
    public void writeData(ByteBuf buffer) {

    }
}
