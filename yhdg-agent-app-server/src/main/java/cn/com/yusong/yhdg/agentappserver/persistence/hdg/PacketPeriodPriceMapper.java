package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketPeriodPriceMapper extends MasterMapper {
    PacketPeriodPrice find(@Param("id") long id);
    List<PacketPeriodPrice> findListByBatteryType(@Param("foregiftId") Long foregiftId, @Param("batteryType") int batteryType, @Param("agentId") int agentId);
    int insert(PacketPeriodPrice packetPeriodPrice);
    int update(PacketPeriodPrice packetPeriodPrice);
    int deleteByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
    int deleteByForegiftId(@Param("foregiftId") long foregiftId);
    int delete(@Param("id") long id);

}
