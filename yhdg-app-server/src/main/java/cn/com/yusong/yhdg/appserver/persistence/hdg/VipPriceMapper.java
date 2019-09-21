package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VipPriceMapper extends MasterMapper {
    VipPrice find(@Param("id") long id);
    VipPrice findByIsActive(@Param("id") long id, @Param("now") Date now);
    VipPrice findOneByStationId(@Param("agentId") int agentId, @Param("batteryType") int batteryType, @Param("stationId") String stationId, @Param("now") Date now);
    VipPrice findOneByCabinetId(@Param("agentId") int agentId, @Param("batteryType") int batteryType, @Param("cabinetId") String cabinetId, @Param("now") Date now);
    VipPrice findOneByCustomerMobile(@Param("agentId") int agentId, @Param("batteryType") int batteryType, @Param("mobile") String mobile, @Param("now")Date now);
    VipPrice findOneByAgentCompanyId(@Param("agentId") int agentId, @Param("batteryType") int batteryType, @Param("agentCompanyId") String agentCompanyId, @Param("now")Date now);
    List<VipPrice> findByBatteryType(@Param("agentId") int agentId, @Param("batteryType") int batteryType);
}
