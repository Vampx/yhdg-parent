package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.frontserver.persistence.yms.PlaylistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService extends AbstractService {
    @Autowired
    PlaylistMapper playlistMapper;

    public Playlist find(long id) {
        return playlistMapper.find(id);
    }
}
