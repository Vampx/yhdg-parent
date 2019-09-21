package cn.com.yusong.yhdg.vehicleserver.comm.server;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.TextMessage;
import cn.com.yusong.yhdg.common.tool.netty.*;
import cn.com.yusong.yhdg.common.tool.netty.VehicleBizFactory;
import cn.com.yusong.yhdg.vehicleserver.comm.session.Session;
import cn.com.yusong.yhdg.vehicleserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.vehicleserver.comm.session.VehicleSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class MainServerHandler extends SimpleChannelInboundHandler<String> {

    static final Logger log = LogManager.getLogger(MainServerHandler.class);

    VehicleBizFactory bizFactory;
    SessionManager sessionManager;
    ExecutorService executorService;

    protected Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();


    public MainServerHandler(VehicleBizFactory bizFactory, ExecutorService executorService, SessionManager sessionManager) {
        this.bizFactory = bizFactory;
        this.sessionManager = sessionManager;
        this.executorService = executorService;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String text) throws Exception {

        if(log.isDebugEnabled()) {
            log.debug("this.instance: {}", this);
            log.debug("ChannelHandlerContext.instance: {}", ctx);
            log.debug("from:{}, recv:{}", ctx.channel().remoteAddress(), text);
        }

        TextMessage message = TextMessage.decode(text);
        if (message == null) {
            log.error("不可识别消息, text={}", text);
            return;
        }

        Biz biz = bizFactory.create(message.getMsgCode());
        if(biz != null) {

//            if(biz instanceof HighPriorityBiz) {
//                highExecutorService.submit(processor);
//            } else {
                MessageProcessor processor = new MessageProcessor();
                processor.channelHandlerContext = ctx;
                processor.message = message;
                processor.biz = biz;
                processor.attributes = attributes;
                executorService.submit(processor);
//            }
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
            VehicleSession value = sessionManager.getVehicleSession(session.id);
            if(value != null && ctx == value.handlerContext) {
                sessionManager.remove(session.id);
                if(log.isDebugEnabled()) {
                    log.debug("sessionManager.remove: {}", session.id);
                }
            }
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("exceptionCaught", cause);
    }
}
