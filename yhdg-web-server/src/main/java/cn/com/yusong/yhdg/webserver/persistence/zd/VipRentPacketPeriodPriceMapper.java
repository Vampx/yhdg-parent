package cn.com.yusong.yhdg.webserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.VipRentPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentPacketPeriodPriceMapper extends MasterMapper {
    VipRentPeriodPrice find(@Param("id") long id);
    List<VipRentPeriodPrice> findListByForegiftId(@Param("foregiftId") Integer vipForegiftId,
                                                  @Param("batteryType") int batteryType,
                                                  @Param("agentId") int agentId,
                                                  @Param("priceId") long priceId);
    int insert(VipRentPeriodPrice vipRentPeriodPrice);
    int update(VipRentPeriodPrice vipRentPeriodPrice);
    int delete(long id);
}
