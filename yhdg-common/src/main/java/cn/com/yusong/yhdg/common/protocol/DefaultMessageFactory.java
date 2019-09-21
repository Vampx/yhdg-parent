package cn.com.yusong.yhdg.common.protocol;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.netty.MessageFactory;

public class DefaultMessageFactory implements MessageFactory {

    @Override
    public Class<? extends Message> getClass(int msgCode) {
        return MsgCode.get(msgCode);
    }
}
