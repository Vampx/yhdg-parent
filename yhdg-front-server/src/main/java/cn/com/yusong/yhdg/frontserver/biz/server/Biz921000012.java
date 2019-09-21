package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.common.protocol.msg92.Interface922000012;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000012;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000012;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.*;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 查询播放列表内容
 */
@Component
public class Biz921000012 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000012.class);

    @Autowired
    AppConfig config;
    @Autowired
    TerminalService terminalService;
    @Autowired
    PlaylistService playlistService;
    @Autowired
    PublishedPlaylistService publishedPlaylistService;
    @Autowired
    PublishedPlaylistDetailService publishedPlaylistDetailService;
    @Autowired
    PublishedMaterialService publishedMaterialService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000012 request = (Msg921000012) message;
        Msg922000012 response = new Msg922000012();
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
        if (request.playlistId == 0) {
            //终端没有关联播放列表
            response.rtnCode = RespCode.CODE_2.getValue();
            response.rtnMsg = "终端没有关联播放列表!";
            writeAndFlush(context, response);
            return;
        }
        Playlist playlist = playlistService.find(request.playlistId);
        PublishedPlaylist publishedPlaylist = publishedPlaylistService.findByVersion(playlist.getId(), playlist.getVersion());
        response.id = request.playlistId;
        response.version = publishedPlaylist.getVersion();
        if(publishedPlaylist != null){
            List<PublishedPlaylistDetail> detais = publishedPlaylistDetailService.findByPlaylist(publishedPlaylist.getId());
            if(!detais.isEmpty()){
                for (PublishedPlaylistDetail publishedPlaylistDetail : detais) {
                    Interface922000012.Detail detail = new Interface922000012.Detail();
                    detail.name = publishedPlaylistDetail.getDetailName();
                    if (publishedPlaylistDetail.getBeginTime() != null) {
                        detail.beginTime = publishedPlaylistDetail.getBeginTime().getTime() + "";
                    }
                    if (publishedPlaylistDetail.getEndTime() != null) {
                        detail.endTime = publishedPlaylistDetail.getEndTime().getTime() + "";
                    }

                    List<PublishedMaterial> materialList = publishedMaterialService.findByDetail(publishedPlaylistDetail.getId());

                    for (PublishedMaterial material : materialList) {
                        Interface922000012.File file = new Interface922000012.File();
                        file.duration = material.getDuration();
                        file.length = material.getSize();
                        file.md5 = material.getMd5Sum();
                        file.name = material.getMaterialName();
                        file.path = material.getFtpPath();
                        detail.fileList.add(file);
                    }

                    response.detailList.add(detail);
                }
            }
        }
        writeAndFlush(context, response);
    }
}
