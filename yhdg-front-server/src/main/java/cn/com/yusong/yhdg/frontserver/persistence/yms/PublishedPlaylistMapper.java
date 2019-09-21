package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublishedPlaylistMapper extends MasterMapper {
    PublishedPlaylist find(long id);

    PublishedPlaylist findByVersion(@Param("playlistId") long playlistId, @Param("version") int version);

}
