package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylistDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublishedPlaylistDetailMapper extends MasterMapper {
    int insert(PublishedPlaylistDetail publishedPlaylistDetail);

    List<PublishedPlaylistDetail> findByPlaylist(@Param("playlistId") long playlistId);

    PublishedPlaylistDetail find(long id);

    Integer hasRecordByProperty(@Param("num") int num);

    int deleteByPlaylist(@Param("playlistId") Long playlistId);
}
