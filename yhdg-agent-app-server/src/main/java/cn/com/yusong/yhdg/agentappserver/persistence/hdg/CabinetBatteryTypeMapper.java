package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CabinetBatteryTypeMapper extends MasterMapper {
    List<CabinetBatteryType> findListByCabinet(@Param("cabinetId") String cabinetId);
    List<CabinetBatteryType> findListByBatteryType(@Param("batteryType") Integer batteryType, @Param("agentId") Integer agentId);
    CabinetBatteryType findAll(@Param("cabinetId") String cabinetId, @Param("batteryType") Integer batteryType);
    int insert(CabinetBatteryType cabinetBatteryType);
    int delete(@Param("cabinetId") String cabinetId, @Param("batteryType") Integer batteryType);

}
