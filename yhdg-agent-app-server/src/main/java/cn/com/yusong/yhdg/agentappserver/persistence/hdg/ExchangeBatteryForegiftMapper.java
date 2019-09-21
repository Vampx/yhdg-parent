package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeBatteryForegiftMapper extends MasterMapper {
	ExchangeBatteryForegift find(@Param("id") long id);
	ExchangeBatteryForegift findByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	List<ExchangeBatteryForegift> findList(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	List<ExchangeBatteryForegift> findListByAgentId(@Param("agentId") int agentId);
	int insert(ExchangeBatteryForegift exchangeBatteryForegift);
	int update(ExchangeBatteryForegift exchangeBatteryForegift);
	int deleteByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	int delete(@Param("id") long id);
}
