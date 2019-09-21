package cn.com.yusong.yhdg.staticserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylistDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublishedPlaylistDetailMapper extends MasterMapper {
    List<PublishedPlaylistDetail> findByPlaylist(@Param("playlistId") long playlistId);
}
