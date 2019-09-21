package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface AgentTotalStatsMapper extends MasterMapper {
    AgentTotalStats find(@Param("agentId") Integer agentId, @Param("category") Integer category);
}
