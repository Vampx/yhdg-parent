package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPacketPeriodPriceMapper extends MasterMapper {
    VipPacketPeriodPrice find(@Param("id") long id);
    List<VipPacketPeriodPrice> findListByForegiftId(@Param("foregiftId") Integer foregiftId,
                                                    @Param("batteryType") Integer batteryType,
                                                    @Param("agentId") Integer agentId,
                                                    @Param("priceId") Long priceId,
                                                    @Param("vipForegiftId") Long vipForegiftId);
    List<VipPacketPeriodPrice> findListByVipForegiftId(@Param("vipForegiftId") Long vipForegiftId);
    List<VipPacketPeriodPrice> findByPriceIdAndForegiftId(@Param("priceId")Long priceId, @Param("foregiftId")Long foregiftId);
    int insert(VipPacketPeriodPrice vipPacketPeriodPrice);
    int update(VipPacketPeriodPrice vipPacketPeriodPrice);
    int updatePriceId(@Param("id") Long id, @Param("priceId") Long priceId);
    int delete(long id);
    int deleteByPriceId(long priceId);

}
