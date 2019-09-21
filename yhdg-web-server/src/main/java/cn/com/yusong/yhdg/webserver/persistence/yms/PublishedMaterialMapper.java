package cn.com.yusong.yhdg.webserver.persistence.yms;
import cn.com.yusong.yhdg.common.domain.yms.PublishedMaterial;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublishedMaterialMapper extends MasterMapper {
    PublishedMaterial find(@Param("materialId") long materialId, @Param("version") int version);

    int insert(PublishedMaterial publishedMaterial);

    List<PublishedMaterial> findByAreaAndDetail(@Param("detailId") long detailId);

    List<PublishedMaterial> findByPlaylist(long playlistId);

    int findCount(@Param("materialId") long materialId);
}
