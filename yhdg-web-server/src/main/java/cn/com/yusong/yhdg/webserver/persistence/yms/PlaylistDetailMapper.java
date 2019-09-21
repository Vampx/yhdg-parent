package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlaylistDetailMapper extends MasterMapper {
    PlayListDetail find(int id);

    PlayListDetail findByPlaylistAndMaterial(@Param("playlistId") int playlistId, @Param("materialId") Long materialId);

    List<PlayListDetail> findByListPlaylist(@Param("playlistId") int playlistId);

    int findPageCount(PlayListDetail playListDetail);

    List<PlayListDetail> findPageResult(PlayListDetail playListDetail);

    int delete(int id);

    int insert(PlayListDetail playListDetail);

    int update(PlayListDetail playListDetail);

    int deleteByPlaylist(@Param("playlistId") int playlistId);

    int findByMaxOrderNum(@Param("playlistId") int playlistId);
}
