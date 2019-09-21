package cn.com.yusong.yhdg.batteryserver.comm.server;

import cn.com.yusong.yhdg.batteryserver.comm.session.BatterySession;
import cn.com.yusong.yhdg.batteryserver.comm.session.Session;
import cn.com.yusong.yhdg.batteryserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.common.protocol.msg06.Msg061;
import cn.com.yusong.yhdg.common.tool.netty.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainServerHandler extends NettyHandler {

    static final Logger log = LogManager.getLogger(MainServerHandler.class);

    SessionManager sessionManager;
    ExecutorService[] executorServices;


    public MainServerHandler(BizFactory bizFactory,  ExecutorService[] executorServices,  SessionManager sessionManager) {
        super(bizFactory, null);
        this.sessionManager = sessionManager;
        this.executorServices = executorServices;
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

            int index = (int) (Math.random() * executorServices.length);
            String code = null;

            if(message instanceof Msg061) {
                String json = ((Msg061) message).json;
                Pattern p = Pattern.compile("IMEI:\"(.*?)\"");
                Matcher matcher = p.matcher(json);
                while (matcher.find()) {
                    code = matcher.group();
                }
            }
            if(StringUtils.isNotEmpty(code)){
                index =  Math.abs((code.hashCode()))  % executorServices.length;
                log.debug("index: {}", index);
            }

            executorServices[index].submit(processor);

            if(log.isDebugEnabled()) {
                log.debug("executorService {}", executorServices[index]);
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
//
//        Session session = (Session) attributes.get(Session.SESSION_KEY);
//        if(session != null) {
//            BatterySession value = sessionManager.getBatterySession(session.id);
//            if(value != null && ctx == value.handlerContext) {
//                sessionManager.remove(session.id);
//                if(log.isDebugEnabled()) {
//                    log.debug("sessionManager.remove: {}", session.id);
//                }
//            }
//        }
//
//        if(sessionManager.getStaticServerClient() != null && sessionManager.getStaticServerClient() == ctx) {
//            sessionManager.setStaticServerClient(null);
//        }

        super.channelInactive(ctx);
    }

    public static void main(String[] args) {
        String json = "{IMEI:\"861929041515501\",BmsVer:\"2.2\",Sig:18,SigType:0,LBS:\"460,00,501b,cac8\",LocType:1,Lng:\"\",Lat:\"\",Vol:70208,Cur:-514,Cells:17,VolList:\"4124,4129,4128,4126,4131,4136,4135,4133,4136,4129,4129,4128,4123,4129,4134,4132,4126\",Bal:0,Temp:\"2992,2999,2991,2995,3001\",ResCap:59995,Soc:999,MaxCap:2160000000,Circle:45,MOS:3,Fault:16384,Heart:30,Motion:1,Uncap:0,Mode:0,BatteryLease:26,Bottom:0,Upper:0,Protect:\"0,0,1,49,4,0,0,0,0,0,0\"}";
        Pattern p = Pattern.compile("IMEI:\"(.*?)\"");
        Matcher matcher = p.matcher(json);
        while (matcher.find()) {
            String code = matcher.group();
            code = code.replace("IMEI:\"", "").replace("\"", "");
            System.out.println(code);
        }
    }
}
