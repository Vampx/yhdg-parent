package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.protocol.msg91.Msg911000009;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000009;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg932000009;
import cn.com.yusong.yhdg.frontserver.biz.server.AbstractBiz;
import cn.com.yusong.yhdg.frontserver.comm.session.Session;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalUploadLogService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 上传终端日志通知
 */
@Component
public class Biz931000009 extends AbstractBiz {

    @Autowired
    TerminalUploadLogService terminalUploadLogService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg931000009 request = (Msg931000009) message;
        Msg932000009 response = new Msg932000009();
        response.setSerial(request.getSerial());

        Session session = config.sessionManager.getSession(request.terminalId);

        if (session != null) {
            Msg911000009 msg = new Msg911000009();
            msg.logId = request.logId;
            msg.type = request.type;
            msg.logTime = request.logTime;
            msg.setSerial(request.getSerial());
            msg.setSerial((int) sequence.incrementAndGet());

            config.sessionManager.writeAndFlush(request.terminalId, msg);
        } else {
            response.rtnCode = RespCode.CODE_3.getValue();
        }

        if (response.rtnCode == 0) {
            //更新状态
            TerminalUploadLog terminalUploadLog = new TerminalUploadLog();
            terminalUploadLog.setId(request.logId);
            terminalUploadLog.setStatus(TerminalUploadLog.Status.NOTICE.getValue());
            terminalUploadLogService.updateStatus(terminalUploadLog);
        }

        context.writeAndFlush(response);
    }
}
