package cn.com.yusong.yhdg.frontserver.comm.server;

import cn.com.yusong.yhdg.common.protocol.msg08.Msg081;
import cn.com.yusong.yhdg.common.tool.netty.*;
import cn.com.yusong.yhdg.frontserver.comm.session.Session;
import cn.com.yusong.yhdg.frontserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class MainServerHandler extends NettyHandler {

    static final Logger log = LogManager.getLogger(MainServerHandler.class);

    SessionManager sessionManager;
    ExecutorService[] normalExecutorService;


    public MainServerHandler(BizFactory bizFactory, ExecutorService[] normalExecutorService, SessionManager sessionManager) {
        super(bizFactory, null);
        this.sessionManager = sessionManager;
        this.normalExecutorService = normalExecutorService;
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


                int index = 0;
                if(message instanceof Msg081) {
                    index = Math.abs(((Msg081) message).getCode().hashCode()) % normalExecutorService.length;
                } else {
                    index = (int) (Math.random() * normalExecutorService.length);
                }
                normalExecutorService[index].submit(processor);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("this.instance: {}", this);
            log.debug("ChannelHandlerContext.instance: {}", ctx);
            log.debug("channelInactive");
        }

        Session session = (Session) attributes.get(Session.SESSION_KEY);
        if(session != null) {
            TerminalSession value = sessionManager.getTerminalSession(session.id);
            if(value != null && ctx == value.handlerContext) {
                sessionManager.remove(session.id);
                if(log.isDebugEnabled()) {
                    log.debug("sessionManager.remove: {}", session.id);
                }
            }
        }

        super.channelInactive(ctx);
    }
}
