package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RotateImageMapper extends MasterMapper {

    public List<RotateImage> findTypeAndSourceIdAll(@Param("type") int type, @Param("sourceId") int sourceId, @Param("category") int category);

}
