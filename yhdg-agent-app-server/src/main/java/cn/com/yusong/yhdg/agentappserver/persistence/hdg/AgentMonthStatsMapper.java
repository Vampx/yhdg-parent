package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMonthStatsMapper extends MasterMapper {
    AgentMonthStats find(@Param("agentId") Integer agentId, @Param("statsMonth") String statsMonth, @Param("category") Integer category);
    List<AgentMonthStats> findList(@Param("agentId") int agentId, @Param("category") Integer category);
    AgentMonthStats findTotal(@Param("agentId") int agentId, @Param("category") Integer category);

}
