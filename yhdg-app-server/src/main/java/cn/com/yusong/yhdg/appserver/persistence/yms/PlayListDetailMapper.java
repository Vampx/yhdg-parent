package cn.com.yusong.yhdg.appserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayListDetailMapper extends MasterMapper {
    public List<PlayListDetail> findListByPlaylistId(@Param("playlistId") int playlistId);

}
