package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg91.Msg911000007;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000007;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg932000007;
import cn.com.yusong.yhdg.frontserver.biz.server.AbstractBiz;
import cn.com.yusong.yhdg.frontserver.comm.session.Session;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 终端配置更新
 */
@Component
public class Biz931000007 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg931000007 request = (Msg931000007) message;
        Msg932000007 response = new Msg932000007();
        response.setSerial(request.getSerial());

        Msg911000007 msg = new Msg911000007();
        msg.setSerial((int) sequence.incrementAndGet());
        msg.terminalName = request.terminalName;
        msg.strategyUid = request.strategyUid;
        msg.logLevel = request.logLevel;
        msg.ftpEncoding = config.ftpEncoding;
        msg.ftpUser = config.ftpUser;
        msg.ftpPassword = config.ftpPassword;
        msg.ftpPort = config.ftpPort;
        msg.playlistId = request.playlistId;
        msg.playlistName = request.playlistName;
        msg.propertyList = request.propertyList;

        /**
         * 替换session中的playlistId
         */
//        Session session = config.sessionManager.getSession(request.terminalId);
//        config.sessionManager.remove(request.terminalId);
//        if (session != null) {
//            config.sessionManager.addSession(config.sessionManager, session.handlerContext, session.attributes, request.terminalId, request.playlistId);
//        }

        TerminalSession session = (TerminalSession) config.sessionManager.getSession(request.terminalId);
        if(session != null) {
            session.playlistId = request.playlistId;
        }

        config.sessionManager.writeAndFlush(request.terminalId, msg);
        writeAndFlush(context, response);
    }

}
