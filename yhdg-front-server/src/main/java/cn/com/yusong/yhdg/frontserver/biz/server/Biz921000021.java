package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000021;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000021;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 请求下载
 */
@Component
public class Biz921000021 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000021.class);

    @Autowired
    TerminalService terminalService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000021 request = (Msg921000021) message;
        Msg922000021 response = new Msg922000021();
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

        boolean permit;
        try{
            log.info("终端ID ：{}！", terminal.getId());
            permit = config.sessionManager.isPermitDownload(terminal.getId(), request.playlistId);
        }catch (Exception e) {
            permit = false;
            log.error("sessionManager.isPermitDownload()方法出现错误 ：{}！", e);
        }

        response.download = permit ? (byte) 1 : 0;

        writeAndFlush(context, response);
    }
}
