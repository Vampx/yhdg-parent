package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCrashLog;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000026;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000026;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalCrashLogService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 上传崩溃日志
 */
@Component
public class Biz921000026 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000026.class);

    @Autowired
    TerminalService terminalService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalCrashLogService terminalCrashLogService;

    public final static String PATH = "/static/download/";

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000026 request = (Msg921000026) message;
        Msg922000026 response = new Msg922000026();
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

        Cabinet cabinet = cabinetService.findTerminal(session.id);
        for(Msg921000026.File e : request.fileList) {
            String time = StringUtils.substringBetween(e.path, "[", "]");
            TerminalCrashLog crashLog = new TerminalCrashLog();
            crashLog.setTerminalId(terminal.getId());
            if (cabinet != null) {
                crashLog.setAgentId(cabinet.getAgentId());
            }

            crashLog.setReportTime(new Date(Long.parseLong(time)));
            crashLog.setFilePath(PATH + e.path);
            crashLog.setCreateTime(new Date());
            terminalCrashLogService.insert(crashLog);
        }

        response.rtnCode = RespCode.CODE_0.getValue();
        writeAndFlush(context, response);
        return;
    }

}
