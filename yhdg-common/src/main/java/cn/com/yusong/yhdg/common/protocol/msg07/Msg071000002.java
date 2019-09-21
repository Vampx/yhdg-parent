package cn.com.yusong.yhdg.common.protocol.msg07;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

/**
 * 开关门查询通知
 */
public class Msg071000002 extends Msg071 {

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_071000002.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
    }

    @Override
    public void writeData(ByteBuf buffer) {
    }
}
