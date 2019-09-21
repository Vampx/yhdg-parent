package cn.com.yusong.yhdg.common.tool.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class NettyHandler extends SimpleChannelInboundHandler<Message> {

    final static Logger log = LogManager.getLogger(NettyHandler.class);

    protected BizFactory bizFactory;
    protected ExecutorService executorService;
    protected Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    public NettyHandler(BizFactory bizFactory, ExecutorService executorService) {
        this.bizFactory = bizFactory;
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

        log.debug("executorService {}", executorService);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if(cause instanceof ReadTimeoutException) {
            if(log.isInfoEnabled()) {
                log.info("session timout, {}", ctx.channel().remoteAddress());
            }
        }

        log.error("error", cause);
        ctx.close();
        if(log.isDebugEnabled()) {
            log.debug("invoke close()");
        }
    }
}
