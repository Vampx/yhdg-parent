package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPriceRenew;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPacketPeriodPriceRenewMapper extends MasterMapper {
    VipPacketPeriodPriceRenew find(@Param("id") long id);
    List<VipPacketPeriodPriceRenew> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType, @Param("foregiftId") Long foregiftId);
}
