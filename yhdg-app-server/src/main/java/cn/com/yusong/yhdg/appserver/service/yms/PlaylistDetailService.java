package cn.com.yusong.yhdg.appserver.service.yms;

import cn.com.yusong.yhdg.appserver.persistence.yms.PlayListDetailMapper;
import cn.com.yusong.yhdg.appserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistDetailService extends AbstractService {
    @Autowired
    PlayListDetailMapper playListDetailMapper;

    public List<PlayListDetail> findListByPlaylistId(int playlistId) {
        return playListDetailMapper.findListByPlaylistId(playlistId);
    }

}
