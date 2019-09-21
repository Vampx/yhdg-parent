package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalPlayLog;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000029;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000029;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 上传播放日志
 */
@Component
public class Biz921000029 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000029.class);

    @Autowired
    TerminalService terminalService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000029 request = (Msg921000029) message;
        Msg922000029 response = new Msg922000029();
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

        response.rtnCode = RespCode.CODE_0.getValue();
        writeAndFlush(context, response);
        return;
    }

}
