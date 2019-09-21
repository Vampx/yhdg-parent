package cn.com.yusong.yhdg.staticserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PublishedPlaylistMapper extends MasterMapper {
    PublishedPlaylist findByVersion(@Param("id") long id, @Param("version") int version);
}
