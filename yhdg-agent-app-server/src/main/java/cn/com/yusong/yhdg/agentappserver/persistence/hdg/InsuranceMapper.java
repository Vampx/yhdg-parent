package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InsuranceMapper extends MasterMapper {
    Insurance find(@Param("id") long id);
    List<Insurance> findByAgent(@Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);
    List<Insurance> findListByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
    int insert(Insurance insurance);
    int delete(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
}
