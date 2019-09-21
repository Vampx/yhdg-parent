package cn.com.yusong.yhdg.agentserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketPeriodPriceMapper extends MasterMapper {
    List<PacketPeriodPrice> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType, @Param("foregiftId") Long foregiftId);
    PacketPeriodPrice find(@Param("id") long id);
    List<PacketPeriodPrice> findListByBatteryType( @Param("batteryType") int batteryType, @Param("agentId") int agentId);
    List<PacketPeriodPrice> findListByForegiftId(@Param("foregiftId") Integer foregiftId, @Param("batteryType") int batteryType, @Param("agentId") int agentId);
    int insert(PacketPeriodPrice packetPeriodPrice);
    int update(PacketPeriodPrice packetPeriodPrice);
    int delete(long id);
    int deleteByForegiftId(@Param("foregiftId") int foregiftId);
}
