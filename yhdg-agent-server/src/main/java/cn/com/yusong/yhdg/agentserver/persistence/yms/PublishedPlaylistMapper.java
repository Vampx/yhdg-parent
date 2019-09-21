package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublishedPlaylistMapper extends MasterMapper {
    PublishedPlaylist find(long id);

    PublishedPlaylist findByVersion(@Param("id") long id, @Param("version") int version);

    int findPageCount(PublishedPlaylist publishedPlaylist);

    List findPageResult(PublishedPlaylist publishedPlaylist);

    List<PublishedPlaylist> findByAgent(@Param("agentId") int agentId);

    List<PublishedPlaylist> findByPlaylist(@Param("playlistId") long playlistId);

    int insert(PublishedPlaylist publishedPlaylist);

    int deleteByPlaylist(@Param("playlistId") long playlistId);

}
