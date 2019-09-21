package cn.com.yusong.yhdg.cabinetserver.comm.server;

import cn.com.yusong.yhdg.common.protocol.msg08.Msg081;
import cn.com.yusong.yhdg.common.tool.netty.*;
import cn.com.yusong.yhdg.cabinetserver.biz.server.HighPriorityBiz;
import cn.com.yusong.yhdg.cabinetserver.comm.session.Session;
import cn.com.yusong.yhdg.cabinetserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.cabinetserver.comm.session.CabinetSession;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class MainServerHandler extends NettyHandler {

    static final Logger log = LogManager.getLogger(MainServerHandler.class);

    SessionManager sessionManager;
    ExecutorService highExecutorService;
    ExecutorService[] normalExecutorService;


    public MainServerHandler(BizFactory bizFactory, ExecutorService highExecutorService, ExecutorService[] normalExecutorService, SessionManager sessionManager) {
        super(bizFactory, null);
        this.sessionManager = sessionManager;
        this.highExecutorService = highExecutorService;
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

            if(biz instanceof HighPriorityBiz) {
                highExecutorService.submit(processor);
            } else {
                int index = 0;
                if(message instanceof Msg081) {
                    index = Math.abs(((Msg081) message).getCode().hashCode()) % normalExecutorService.length;
                } else {
                    index = (int) (Math.random() * normalExecutorService.length);
                }
                normalExecutorService[index].submit(processor);
            }
        }

        if(log.isDebugEnabled()) {
            log.debug("highExecutorService {}", highExecutorService);
            for(ExecutorService e : normalExecutorService) {
                log.debug("normalExecutorService {}", e);
            }
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
            CabinetSession value = sessionManager.getCabinetSession(session.id);
            if(value != null && ctx == value.handlerContext) {
                sessionManager.remove(session.id);
                if(log.isDebugEnabled()) {
                    log.debug("sessionManager.remove: {}", session.id);
                }
            }
        }

        if(sessionManager.getStaticServerClient() != null && sessionManager.getStaticServerClient() == ctx) {
            sessionManager.setStaticServerClient(null);
        }

        super.channelInactive(ctx);
    }
}
