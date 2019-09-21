package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentMonthStatsMapper extends MasterMapper {
    public AgentMonthStats find(@Param("agentId") int agentId, @Param("statsMonth") String statsMonth, @Param("category") Integer category);
    public PlatformDayStats sumAll();
    public int insert(AgentMonthStats stats);
    public int update(AgentMonthStats stats);
}
