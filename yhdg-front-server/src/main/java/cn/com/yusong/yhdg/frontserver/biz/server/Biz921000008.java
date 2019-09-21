package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalDownloadProgress;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000008;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000008;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalDownloadProgressService;
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
 * 上报下载进度(播放列表)
 */
@Component
public class Biz921000008 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000008.class);

    @Autowired
    AppConfig config;
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalOnlineService terminalOnlineService;
    @Autowired
    TerminalDownloadProgressService terminalDownloadProgressService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000008 request = (Msg921000008) message;
        Msg922000008 response = new Msg922000008();
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
        if(request.percent > 1) {
            request.percent = 1;
        }

        terminalOnlineService.updateDownloadPlaylistProgress(session.id, request.speed, request.percent);


        Map<String, Object> data = new HashMap<String, Object>();
        data.put("playlistUid", request.playlistUid);
        data.put("speed", request.speed);
        data.put("percent", request.percent);
        data.put("fileList", request.fileList);
        String playlistProgressInfo = AppUtils.encodeJson(data);

        TerminalDownloadProgress progress = new TerminalDownloadProgress();
        progress.setPlaylistProgressInfo(playlistProgressInfo);
        progress.setId(terminal.getId());
        terminalDownloadProgressService.update(progress);

        writeAndFlush(context, response);
    }
}
