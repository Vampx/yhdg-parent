package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;


public interface PlaylistMapper extends MasterMapper {
    Playlist find(long id);
}
