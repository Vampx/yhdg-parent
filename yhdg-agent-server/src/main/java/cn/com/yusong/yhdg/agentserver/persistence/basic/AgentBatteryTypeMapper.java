package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentBatteryTypeMapper extends MasterMapper {
	AgentBatteryType find(@Param("batteryType") int batteryType, @Param("agentId") int agentId);

	List<AgentBatteryType> findListByCabinetId(@Param("cabinetId") String cabinetId);

	List<AgentBatteryType> findBatteryListByAgentId(@Param("agentId") int agentId);

	AgentBatteryType findForName(@Param("batteryType") int batteryType, @Param("agentId") int agentId);

	int findCountByBatteryType(@Param("batteryType") int batteryType);

	List<AgentBatteryType> findListByBatteryType(@Param("batteryType") int batteryType);

	List<AgentBatteryType> findListByAgentId(@Param("agentId") int agentId);

	int findPageCount(AgentBatteryType agentBatteryType);

	List<AgentBatteryType> findPageResult(AgentBatteryType agentBatteryType);

	int insert(AgentBatteryType agentBatteryType);

	int update(@Param("batteryType") int batteryType, @Param("typeName") String typeName, @Param("fromBatteryType") int fromBatteryType, @Param("agentId") int agentId);

	int delete(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
}
