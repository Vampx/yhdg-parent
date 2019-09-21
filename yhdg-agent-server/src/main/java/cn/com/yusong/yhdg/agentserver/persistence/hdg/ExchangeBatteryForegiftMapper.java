package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeBatteryForegiftMapper extends MasterMapper {
	List<ExchangeBatteryForegift> findByAgent(@Param("agentId") int agentId, @Param("batteryType") Integer batteryType);
	ExchangeBatteryForegift find(@Param("id") long id);
	ExchangeBatteryForegift findByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	List<ExchangeBatteryForegift> findListByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
	int insert(ExchangeBatteryForegift exchangeBatteryForegift);
	int update(@Param("money") int money, @Param("batteryType") int batteryType, @Param("agentId") int agentId, @Param("memo") String memo, @Param("id") long id);
	int delete(@Param("id") long id);
}
