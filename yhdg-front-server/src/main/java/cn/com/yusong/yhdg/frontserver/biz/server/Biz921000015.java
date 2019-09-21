package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000015;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000015;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalOnlineService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 播放状态上报
 */
@Component
public class Biz921000015 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000015.class);

    @Autowired
    AppConfig config;
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalOnlineService terminalOnlineService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000015 request = (Msg921000015) message;
        Msg922000015 response = new Msg922000015();
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

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("playlistName", request.playlistName);
        data.put("detailName", request.detailName);
        data.put("materialName", request.materialName);
        String playFile = AppUtils.encodeJson(data);
        terminalOnlineService.updatePlayFile(session.id, playFile);

        writeAndFlush(context, response);
    }
}
