package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Part;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PartMapper extends MasterMapper {
    Part find(Integer id);
    int findUnique(@Param("id") Integer id, @Param("partName") String partName);
    List<Part> findList(@Param("mobile") String mobile, @Param("partType") Integer partType);
    int findPageCount(Part part);
    List<Part> findPageResult(Part part);
    int insert(Part part);
    int update(Part part);
    int delete(Integer id);
}
