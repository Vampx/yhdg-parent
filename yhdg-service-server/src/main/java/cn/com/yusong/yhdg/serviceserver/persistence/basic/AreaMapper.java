package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaMapper extends MasterMapper {
    public Area find(@Param("id") int id);
    public List<Area> findAll();
    public List<Area> findByParentId(@Param("parentId") int parentId);
    public Area findByCodeAndName(@Param("areaCode") String areaCode, @Param("areaName") String areaName);
}
