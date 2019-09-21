package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.ExchangePriceTime;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangePriceTimeMapper extends MasterMapper {
    ExchangePriceTime findByBatteryType(@Param("agentId") int agentId, @Param("batteryType")  int batteryType);
}
