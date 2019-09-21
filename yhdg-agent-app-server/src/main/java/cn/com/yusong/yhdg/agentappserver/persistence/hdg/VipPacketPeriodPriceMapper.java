package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPacketPeriodPriceMapper extends MasterMapper {
    List<VipPacketPeriodPrice> findListByForegiftId(@Param("foregiftId") Long foregiftId,
                                                    @Param("batteryType") int batteryType,
                                                    @Param("agentId") int agentId,
                                                    @Param("priceId") long priceId);
    List<VipPacketPeriodPrice> findListByPriceId(@Param("priceId") long priceId);
    int insert(VipPacketPeriodPrice vipPacketPeriodPrice);
    int deleteByPriceId(Long priceId);
}
