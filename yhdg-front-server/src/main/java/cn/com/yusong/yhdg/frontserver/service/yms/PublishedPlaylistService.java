package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.frontserver.persistence.yms.PublishedPlaylistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishedPlaylistService {

    @Autowired
    PublishedPlaylistMapper publishedPlaylistMapper;

    public PublishedPlaylist find(long id) {
        return publishedPlaylistMapper.find(id);
    }

    public PublishedPlaylist findByVersion(long playlistId, int version) {
        return publishedPlaylistMapper.findByVersion(playlistId, version);
    }

}
