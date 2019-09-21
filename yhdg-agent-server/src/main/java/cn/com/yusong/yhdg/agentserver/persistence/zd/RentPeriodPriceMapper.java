package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentPeriodPriceMapper extends MasterMapper {
    List<RentPeriodPrice> findListByForegift(@Param("foregiftId") Long foregiftId,
                                             @Param("batteryType") Integer batteryType,
                                             @Param("agentId") Integer agentId);

    List<RentPeriodPrice> findListByForegiftId(@Param("foregiftId") Long foregiftId, @Param("batteryType") int batteryType, @Param("agentId") int agentId);

    RentPeriodPrice find(@Param("id") long id);

    List<RentPeriodPrice> findListByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);

    int deleteByForegift(@Param("foregiftId") Integer foregiftId);

    int insert(RentPeriodPrice entity);

    int update(RentPeriodPrice entity);

    int delete(long id);
}
