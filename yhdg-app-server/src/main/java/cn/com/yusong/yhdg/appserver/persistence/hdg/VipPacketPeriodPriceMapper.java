package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPacketPeriodPriceMapper extends MasterMapper {
    VipPacketPeriodPrice find(@Param("id") long id);
    int findCountByForegiftId(@Param("priceId")Long priceId, @Param("foregiftId") long foregiftId);
    List<VipPacketPeriodPrice> findByPriceIdAndForegiftId(@Param("priceId")Long priceId, @Param("foregiftId")Long foregiftId);
}
