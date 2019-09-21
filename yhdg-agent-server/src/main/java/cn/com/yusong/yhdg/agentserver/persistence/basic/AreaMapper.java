package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/5/18.
 */
public interface AreaMapper extends MasterMapper {
    Area find(int id);
    List<Area> findChildren(int id);
    List<Area> findAll();
    List<Area> findAllCity();
    List<Area> findProvinceForCharger();
    List<Area> findCityByProvinceForCharger(@Param("provinceId") Integer provinceId);
}
