package cn.com.yusong.yhdg.serviceserver.comm.server;

import cn.com.yusong.yhdg.common.tool.netty.*;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import io.netty.channel.SimpleChannelInboundHandler;

public class MainServer extends NettyServer {
    BizFactory bizFactory = new DefaultBizFactory();
    AppConfig config;

    public MainServer(AppConfig config) {
        super(config.getServerPort());
        this.config = config;
        config.mainServer = this;
    }

    @Override
    protected SimpleChannelInboundHandler<Message> newNettyHandler() {
        return new MainServerHandler(bizFactory, config.executorService);
    }

    @Override
    protected MessageFactory newMessageFactory() {
        return MessageFactory.DEFAULT_INSTANCE;
    }
}
