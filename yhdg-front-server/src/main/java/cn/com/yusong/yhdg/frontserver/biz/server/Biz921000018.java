package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalRunLog;
import cn.com.yusong.yhdg.common.protocol.msg92.Interface921000018;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000018;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000018;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalRunLogService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 信息上报
 */
@Component
public class Biz921000018 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000018.class);

    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalRunLogService terminalRunLogService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000018 request = (Msg921000018)message;
        Msg922000018 response = new Msg922000018();
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

        for(int i = 0, size = request.msgList.size(); i < size; i++) {
            Interface921000018.Msg msg = request.msgList.get(i);
            TerminalRunLog log = new TerminalRunLog();
            log.setTerminalId(terminal.getId());
            log.setNow(msg.time);
            log.setNum((long) request.getSerial());
            log.setAgentId(terminal.getAgentId());
            log.setReportTime(new Date(msg.time));
            log.setLogLevel((int) msg.level);
            log.setTag(msg.tag);
            log.setContent(msg.content);
            log.setCreateTime(new Date());
            terminalRunLogService.insert(log);
        }

        writeAndFlush(context, response);
    }
}
