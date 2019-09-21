package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentTotalStatsMapper extends MasterMapper {
    public AgentTotalStats find(@Param("agentId") int agentId, @Param("category") Integer category);
    public PlatformDayStats sum();
    public int insert(AgentTotalStats stats);
    public int update(AgentTotalStats stats);
}
