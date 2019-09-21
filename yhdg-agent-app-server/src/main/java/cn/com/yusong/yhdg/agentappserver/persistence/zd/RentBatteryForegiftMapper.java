package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentBatteryForegiftMapper extends MasterMapper {
	RentBatteryForegift find(@Param("id") Long id);
	RentBatteryForegift findByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	List<RentBatteryForegift> findList(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	List<RentBatteryForegift> findListByAgentId(@Param("agentId") int agentId);
	int insert(RentBatteryForegift rentBatteryForegift);
	int update(RentBatteryForegift rentBatteryForegift);
	int delete(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	int deleteById(@Param("id") Long id);

}
