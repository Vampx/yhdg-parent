package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentDayStatsMapper extends MasterMapper {
    public int findPageCount(AgentDayStats search);
    public List<AgentDayStats> findPageResult(AgentDayStats search);
    public AgentDayStats findByDate(@Param("agentId") int agentId, @Param("statsDate") String statsDate);
}
