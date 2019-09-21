package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartModel;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PartModelMapper extends MasterMapper {
    PartModel find(Integer id);
    int findUnique(@Param("id") Integer id, @Param("partModelName") String partModelName);
    List<PartModel> findList(@Param("partModelType") Integer partModelType);
    int findPageCount(PartModel partModel);
    List<PartModel> findPageResult(PartModel partModel);
    int insert(PartModel partModel);
    int update(PartModel partModel);
    int delete(Integer id);
}
