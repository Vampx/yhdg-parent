package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.StationBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface StationBatteryTypeMapper extends MasterMapper {
    List<StationBatteryType> findListByBatteryType(@Param("batteryType") Integer batteryType, @Param("agentId") Integer agentId);

    List<StationBatteryType> findListByStation(@Param("stationId") String stationId);

    int insert(StationBatteryType stationBatteryType);

    int delete(@Param("stationId") String stationId, @Param("batteryType") Integer batteryType);
}
