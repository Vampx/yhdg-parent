package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketPeriodPriceMapper extends MasterMapper {
    PacketPeriodPrice find(@Param("id") long id);
    List<PacketPeriodPrice> findListByBatteryType( @Param("batteryType") int batteryType, @Param("agentId") int agentId);
    List<PacketPeriodPrice> findListByForegiftId(@Param("foregiftId") Long foregiftId, @Param("batteryType") int batteryType, @Param("agentId") int agentId);
    List<PacketPeriodPrice> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType, @Param("foregiftId") Long foregiftId);
    int insert(PacketPeriodPrice packetPeriodPrice);
    int update(PacketPeriodPrice packetPeriodPrice);
    int delete(long id);
    int deleteByForegiftId(@Param("foregiftId") int foregiftId);
}
