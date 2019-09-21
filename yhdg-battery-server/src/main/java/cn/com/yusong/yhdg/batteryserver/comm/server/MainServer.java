package cn.com.yusong.yhdg.batteryserver.comm.server;

import cn.com.yusong.yhdg.batteryserver.config.AppConfig;
import cn.com.yusong.yhdg.common.protocol.DefaultMessageFactory;
import cn.com.yusong.yhdg.common.tool.netty.*;
import io.netty.channel.SimpleChannelInboundHandler;

public class MainServer extends NettyServer {
    MessageFactory messageFactory = new DefaultMessageFactory();
    BizFactory bizFactory = new DefaultBizFactory();
    AppConfig config;

    public MainServer(AppConfig config) {
        super(config.getServerPort());
        this.config = config;
        config.mainServer = this;
    }

    @Override
    protected SimpleChannelInboundHandler<Message> newNettyHandler() {
        return new MainServerHandler(bizFactory, config.executorServices, config.sessionManager);
    }

    @Override
    protected MessageFactory newMessageFactory() {
        return messageFactory;
    }
}
