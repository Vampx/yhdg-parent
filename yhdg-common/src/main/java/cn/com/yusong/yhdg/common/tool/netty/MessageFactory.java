package cn.com.yusong.yhdg.common.tool.netty;

import cn.com.yusong.yhdg.common.protocol.DefaultMessageFactory;

public interface MessageFactory {
    public static MessageFactory DEFAULT_INSTANCE = new DefaultMessageFactory();

    public abstract Class<? extends Message> getClass(int msgCode);
}