package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentBatteryTypeMapper extends MasterMapper {
	AgentBatteryType find(@Param("batteryType") int batteryType, @Param("agentId") int agentId);

	List<AgentBatteryType> findListByAgentId(@Param("agentId") int agentId);

	List<AgentBatteryType> findList(@Param("agentId") Integer agentId,
							        @Param("offset") int offset,
									@Param("limit") int limit);
	int insert(AgentBatteryType agentBatteryType);

	int delete(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
}
