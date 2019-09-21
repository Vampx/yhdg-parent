package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.VipRentBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentBatteryForegiftMapper extends MasterMapper {
	VipRentBatteryForegift find(Long id);
	List<VipRentBatteryForegift> findListByPriceId(@Param("priceId") long priceId);
	VipRentBatteryForegift findByAgentIdAndForegiftId(@Param("agentId") int agentId,
                                                          @Param("foregiftId") Long foregiftId,
                                                          @Param("priceId") Long priceId);
	int findByAgentId(@Param("agentId") int agentId, @Param("foregiftId") Long foregiftId);
	int insert(VipRentBatteryForegift vipRentBatteryForegift);
	int update(VipRentBatteryForegift vipRentBatteryForegift);
	int deleteByAgentId(@Param("agentId") int agentId, @Param("foregiftId") Long foregiftId);
	int deleteByPriceId(long priceId);
}
