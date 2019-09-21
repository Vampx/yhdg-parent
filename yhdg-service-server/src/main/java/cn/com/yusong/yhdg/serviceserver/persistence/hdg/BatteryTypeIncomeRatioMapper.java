package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryTypeIncomeRatioMapper extends MasterMapper {
    BatteryTypeIncomeRatio findByBatteryType(@Param("agentId") int agetnId, @Param("batteryType") int batteryType);
}
