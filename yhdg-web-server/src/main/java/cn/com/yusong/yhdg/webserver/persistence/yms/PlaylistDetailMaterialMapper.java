package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.domain.yms.PlaylistDetailMaterial;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhoub on 2017/7/25.
 */
public interface PlaylistDetailMaterialMapper extends MasterMapper {
    List<Material> findByAreaAndDetail(@Param("detailId") long detailId);

    List<PlaylistDetailMaterial> findByDetail(@Param("detailId") long detailId);

    int insert(PlaylistDetailMaterial playlistAreaMaterial);

    PlaylistDetailMaterial find(PlaylistDetailMaterial playlistAreaMaterial);

    int deleteByMaterial(@Param("materialId") long materialId);

    int deleteByDetail(@Param("detailId") long detailId);

    Integer hasRecordByProperty(@Param("property") String property, @Param("value") Object value);

    List<PlaylistDetailMaterial> findByInfo(PlaylistDetailMaterial playlistAreaMaterial);

    int findCountByDetail(@Param("detailId") long detailId);
}
