package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000030;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000030;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalUploadLogService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 上传终端日志
 */
@Component
public class Biz921000030 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000030.class);

    @Autowired
    TerminalService terminalService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalUploadLogService terminalUploadLogService;

    public final static String PATH = "/static/download/";

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000030 request = (Msg921000030) message;
        Msg922000030 response = new Msg922000030();
        response.setSerial(request.getSerial());

        TerminalSession session = getSession(attributes);
        if (session == null) {
            response.rtnCode = RespCode.CODE_7.getValue();
            response.rtnMsg = RespCode.CODE_7.getName();
            writeAndFlush(context, response);
            return;
        }
        log.debug("terminal ID {} ", session.id);

        Terminal terminal = terminalService.find(session.id);
        if (terminal == null) {
            response.rtnCode = RespCode.CODE_3.getValue();
            response.rtnMsg = RespCode.CODE_3.getName();
            writeAndFlush(context, response);
            return;
        }

        String path = PATH + request.path;
        TerminalUploadLog uploadLog = new TerminalUploadLog();
        uploadLog.setId(request.logId);
        if (StringUtils.isEmpty(request.path)) {
            path = "";
        }
        uploadLog.setFilePath(path);
        uploadLog.setStatus(TerminalUploadLog.Status.UPLOAD.getValue());
        uploadLog.setUploadTime(new Date());
        terminalUploadLogService.update(uploadLog);

        response.rtnCode = RespCode.CODE_0.getValue();
        writeAndFlush(context, response);
        return;
    }

}
