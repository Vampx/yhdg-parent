package cn.com.yusong.yhdg.appserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.VipRentPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentPeriodPriceMapper extends MasterMapper {
    VipRentPeriodPrice find(@Param("id") long id);
    int findCountByForegiftId(@Param("priceId") Long priceId, @Param("foregiftId") long foregiftId);
    List<VipRentPeriodPrice> findByPriceIdAndForegiftId(@Param("priceId") Long priceId, @Param("foregiftId") Long foregiftId);
}
