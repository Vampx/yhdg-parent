package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCommand;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000024;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000024;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalCommandService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 上报命令执行状态
 */
@Component
public class Biz921000024 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000024.class);

    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalCommandService terminalCommandService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000024 request = (Msg921000024)message;
        Msg922000024 response = new Msg922000024();
        response.setSerial(request.getSerial());

        TerminalSession session = getSession(attributes);
        if(session == null) {
            response.rtnCode = RespCode.CODE_7.getValue();
            response.rtnMsg = RespCode.CODE_7.getName();
            writeAndFlush(context, response);
            return;
        }
        log.debug("terminal ID {} ", session.id);

        Terminal terminal = terminalService.find(session.id);
        if(terminal == null) {
            response.rtnCode = RespCode.CODE_3.getValue();
            response.rtnMsg = RespCode.CODE_3.getName();
            writeAndFlush(context, response);
            return;
        }

        int status = request.status == 1 ? TerminalCommand.Status.SUCCESS.getValue() : TerminalCommand.Status.FAIL.getValue();
        terminalCommandService.exec(request.commandId, status, new Date(), request.failureReason);

        writeAndFlush(context, response);
    }
}
