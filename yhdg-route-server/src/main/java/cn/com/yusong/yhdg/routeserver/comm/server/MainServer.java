package cn.com.yusong.yhdg.routeserver.comm.server;

import cn.com.yusong.yhdg.common.protocol.DefaultMessageFactory;
import cn.com.yusong.yhdg.common.tool.netty.*;
import cn.com.yusong.yhdg.routeserver.config.AppConfig;
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
        return new MainServerHandler(bizFactory, config.executorService);
    }

    @Override
    protected MessageFactory newMessageFactory() {
        return messageFactory;
    }
}
