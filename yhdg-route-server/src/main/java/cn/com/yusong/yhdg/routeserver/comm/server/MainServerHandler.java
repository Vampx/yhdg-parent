package cn.com.yusong.yhdg.routeserver.comm.server;

import cn.com.yusong.yhdg.common.tool.netty.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class MainServerHandler extends NettyHandler {

    static final Logger log = LogManager.getLogger(MainServerHandler.class);

    ExecutorService executorService;


    public MainServerHandler(BizFactory bizFactory, ExecutorService executorService) {
        super(bizFactory, null);
        this.executorService = executorService;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("this.instance: {}", this);
            log.debug("ChannelHandlerContext.instance: {}", ctx);
            log.debug("from:{}, recv:{}, ", ctx.channel().remoteAddress(), message);
        }

        Biz biz = bizFactory.create(message.getMsgCode());
        if(biz != null) {
            MessageProcessor processor = new MessageProcessor();
            processor.channelHandlerContext = ctx;
            processor.message = message;
            processor.biz = biz;
            processor.attributes = attributes;
            executorService.submit(processor);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("this.instance: {}", this);
            log.debug("ChannelHandlerContext.instance: {}", ctx);
            log.debug("channelInactive");
        }
        super.channelInactive(ctx);
    }
}
