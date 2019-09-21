package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPriceRenew;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketPeriodPriceRenewMapper extends MasterMapper {
    PacketPeriodPriceRenew find(@Param("id") long id);
    List<PacketPeriodPriceRenew> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType, @Param("foregiftId") Long foregiftId);
}
