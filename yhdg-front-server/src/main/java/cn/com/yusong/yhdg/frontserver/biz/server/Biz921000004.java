package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000004;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000004;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.PlaylistService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查询播放列表版本
 */
@Component
public class Biz921000004 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000004.class);

    @Autowired
    AppConfig config;
    @Autowired
    TerminalService terminalService;
    @Autowired
    PlaylistService playlistService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000004 request = (Msg921000004) message;
        Msg922000004 response = new Msg922000004();
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
        if(request.playlistId == 0){
            response.playlistUid = "playlist-0";
            response.exists = 1;
        } else {
            Playlist playlist = playlistService.find(request.playlistId);
            response.playlistUid = String.format("playlist-%d-%d", playlist.getId(), playlist.getVersion());
            response.exists = 1;
        }
        writeAndFlush(context, response);
    }
}
