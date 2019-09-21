package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CabinetBatteryTypeMapper extends MasterMapper {
    List<CabinetBatteryType> findListByBatteryType(@Param("batteryType") Integer batteryType, @Param("agentId") Integer agentId);

    List<CabinetBatteryType> findListByCabinet(@Param("cabinetId") String cabinetId);

    int insert(CabinetBatteryType cabinetBatteryType);

    int delete(@Param("cabinetId") String cabinetId, @Param("batteryType") Integer batteryType);
}
