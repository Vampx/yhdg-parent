package cn.com.yusong.yhdg.staticserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedMaterial;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublishedMaterialMapper extends MasterMapper {
    List<PublishedMaterial> findByDetail(@Param("detailId") long detailId, @Param("status") int status);
}
