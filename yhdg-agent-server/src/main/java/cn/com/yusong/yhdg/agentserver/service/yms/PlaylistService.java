package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PlaylistService extends AbstractService {
    static Logger log = LogManager.getLogger(PlaylistService.class);
    @Autowired
    PlaylistMapper playlistMapper;
    @Autowired
    MaterialMapper materialMapper;
    @Autowired
    PlaylistDetailMaterialMapper playlistDetailMaterialMapper;
    @Autowired
    PlaylistDetailMapper playlistDetailMapper;
    @Autowired
    PublishedPlaylistMapper publishedPlaylistMapper;
    @Autowired
    PublishedPlaylistDetailMaterialMapper publishedPlaylistAreaMaterialMapper;
    @Autowired
    PublishedMaterialMapper publishedMaterialMapper;
    @Autowired
    PublishedPlaylistDetailMapper publishedPlaylistDetailMapper;
    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    PublishedPlaylistDetailMaterialMapper publishedPlaylistDetailMaterialMapper;

    public Playlist find(int id) {
        return playlistMapper.find(id);
    }

    public List<Integer> findAll() {
        return playlistMapper.findAll();
    }

    public Page findPage(Playlist search) {
        Page page = search.buildPage();
        page.setTotalItems(playlistMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Playlist> list = playlistMapper.findPageResult(search);
        for (Playlist playlist : list) {
            if (playlist != null) {
                if (playlist.getAgentId() != null) {
                    playlist.setAgentName(findAgentInfo(playlist.getAgentId()).getAgentName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public List<Playlist> findByAgent(int agentId) {
        return playlistMapper.findByAgent(agentId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(Playlist entity, String username) {
        entity.setCreateTime(new Date());
        entity.setVersion(1);
        //新添加的播放列表为已发布状态
        entity.setStatus(Playlist.Status.PUBLISHED.getValue());
        entity.setAutitTime(new Date());
        entity.setAuditUser(username);
        //新增制作库播放列表
       playlistMapper.insert(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(int id) {
        playlistDetailMapper.deleteByPlaylist(id);
        List<Terminal> terminalList = terminalMapper.findByPlaylist(id);
        if (!terminalList.isEmpty()) {
            for (Terminal terminal : terminalList) {
                terminalMapper.installBlank(terminal.getId());
            }
        }
        int i = playlistMapper.delete(id);
        if (i == 0) {
            return ExtResult.failResult("删除失败，请重试");
        }
        return ExtResult.successResult();
    }
}
