package cn.com.yusong.yhdg.agentserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPacketPeriodPriceMapper extends MasterMapper {
    List<VipPacketPeriodPrice> findByPriceIdAndForegiftId(@Param("priceId")Long priceId, @Param("foregiftId")Long foregiftId);
    VipPacketPeriodPrice find(@Param("id") long id);
    List<VipPacketPeriodPrice> findListByForegiftId(@Param("foregiftId") Integer vipForegiftId,
                                                    @Param("batteryType") int batteryType,
                                                    @Param("agentId") int agentId,
                                                    @Param("priceId") long priceId);
    int insert(VipPacketPeriodPrice vipPacketPeriodPrice);
    int update(VipPacketPeriodPrice vipPacketPeriodPrice);
    int delete(long id);
}
