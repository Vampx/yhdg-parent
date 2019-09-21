package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlaylistMapper extends MasterMapper {
    Playlist find(int id);

    List<Integer> findAll();

    int findPageCount(Playlist playlist);

    List<Playlist> findPageResult(Playlist playlist);

    List<Playlist> findByAgent(@Param("agentId") int agentId);

    int insert(Playlist playlist);

    int delete(int id);

    int update(Playlist playlist);
}
