package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InsuranceMapper extends MasterMapper {
    int findPageCount(Insurance insurance);
    List<Insurance> findPageResult(Insurance insurance);
    List<Insurance> findListByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
    Insurance find(@Param("id") long id);
    int insert(Insurance insurance);
    int update(Insurance insurance);
    int delete(@Param("id") long id);

}
