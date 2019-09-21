package cn.com.yusong.yhdg.appserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PlaylistMapper extends MasterMapper {

    Playlist find(@Param("id") int id);

}
