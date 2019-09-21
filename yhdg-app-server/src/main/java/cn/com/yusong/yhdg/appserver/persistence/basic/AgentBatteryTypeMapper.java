package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentBatteryTypeMapper extends MasterMapper {
	AgentBatteryType find(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
}
