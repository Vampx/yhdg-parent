package cn.com.yusong.yhdg.agentappserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.VipRentPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentPeriodPriceMapper extends MasterMapper {
    List<VipRentPeriodPrice> findListByForegiftId(@Param("foregiftId") Long foregiftId,
                                                    @Param("batteryType") int batteryType,
                                                    @Param("agentId") int agentId,
                                                    @Param("priceId") long priceId);
    int insert(VipRentPeriodPrice vipRentPeriodPrice);
    int deleteByPriceId(Long priceId);
}
