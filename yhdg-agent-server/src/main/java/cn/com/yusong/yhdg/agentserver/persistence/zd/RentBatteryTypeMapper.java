package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentBatteryTypeMapper extends MasterMapper {
    RentBatteryType find(@Param("batteryType") int batteryType, @Param("agentId") int agentId);

    RentBatteryType findByBatteryTypeAndAgent(@Param("batteryType") Integer batteryType, @Param("agentId") Integer
            agentId);

    int findPageCount(RentBatteryType entity);

    List<RentBatteryType> findBatteryListByAgentId(@Param("agentId") int agentId);

    List<RentBatteryType> findPageResult(RentBatteryType entity);

    List<RentBatteryType> findListByAgent(Integer agentId);

    int insert(RentBatteryType entity);

    int update(@Param("batteryType") int batteryType,
               @Param("typeName") String typeName,
               @Param("fromBatteryType") int fromBatteryType,
               @Param("agentId") int agentId);

    int delete(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
}
