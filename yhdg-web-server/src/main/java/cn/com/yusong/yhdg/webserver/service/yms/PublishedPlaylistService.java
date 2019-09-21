package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.yms.PublishedPlaylistMapper;
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

    public PublishedPlaylist findByVersion(long id, int version) {
        return publishedPlaylistMapper.findByVersion(id, version);
    }

    public Page findPage(PublishedPlaylist publishedPlaylist) {
        Page page = publishedPlaylist.buildPage();
        page.setTotalItems(publishedPlaylistMapper.findPageCount(publishedPlaylist));
        publishedPlaylist.setBeginIndex(page.getOffset());
        page.setResult(publishedPlaylistMapper.findPageResult(publishedPlaylist));
        return page;
    }

    public List<PublishedPlaylist> findByAgent(int agentId) {
        return publishedPlaylistMapper.findByAgent(agentId);
    }

}
