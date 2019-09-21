package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.protocol.msg91.Msg911000003;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000003;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg932000003;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.frontserver.biz.server.AbstractBiz;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.AppConst;
import cn.com.yusong.yhdg.frontserver.service.yms.PlaylistService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件更新（播放列表）
 */
@Component
public class Biz931000003 extends AbstractBiz {

    @Autowired
    TerminalService termialService;
    @Autowired
    PlaylistService playlistService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg931000003 request = (Msg931000003) message;
        Msg932000003 response = new Msg932000003();
        response.setSerial(request.getSerial());

        Playlist playlist = playlistService.find(request.playlistId);

        String uid = String.format(AppConst.FORMAT_PLAYLIST_UID, playlist.getId(), playlist.getVersion());
        File file = new File(config.markPlaylistDir, uid);
        if (!file.exists()) {
            AppUtils.makeParentDir(file);
            file.createNewFile();

            Msg911000003 msg911000003 = new Msg911000003();
            msg911000003.playlistId = request.playlistId;
            List<Terminal> terminals = termialService.findByPlaylist(request.playlistId);
            for (Terminal terminal : terminals) {
                TerminalSession terminalSession = config.sessionManager.getTerminalSession(terminal.getId());
                if(terminalSession != null) {
                    terminalSession.playlistId = request.playlistId;
                    config.sessionManager.writeAndFlush(terminal.getId(), msg911000003);
                }
            }
        }

        writeAndFlush(context, response);
    }

}
