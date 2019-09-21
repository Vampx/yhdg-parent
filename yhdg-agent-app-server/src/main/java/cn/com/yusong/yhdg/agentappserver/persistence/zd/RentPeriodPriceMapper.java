package cn.com.yusong.yhdg.agentappserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentPeriodPriceMapper extends MasterMapper {
    RentPeriodPrice find(@Param("id") long id);
    List<RentPeriodPrice> findListByBatteryType(@Param("foregiftId") Long foregiftId, @Param("batteryType") int batteryType, @Param("agentId") int agentId);
    int insert(RentPeriodPrice rentPeriodPrice);
    int update(RentPeriodPrice packetPeriodPrice);
    int deleteByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
    int deleteByForegiftId(@Param("foregiftId") long foregiftId);
    int delete(@Param("id") long id);
}
