package cn.com.yusong.yhdg.appserver.persistence.hdg;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PacketPeriodPriceMapper extends MasterMapper {

	PacketPeriodPrice find(@Param("id") Long id);

	List<PacketPeriodPrice> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType, @Param("foregiftId") Long foregiftId);

	List<Map> findPacketPeriodList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);

}
