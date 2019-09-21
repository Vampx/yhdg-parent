package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AgentDayStatsMapper extends MasterMapper {
    public AgentDayStats find(@Param("agentId") Integer agentId, @Param("statsDate") String statsDate, @Param("category") Integer category);

    public PlatformDayStats sumDay(@Param("statsDate") String statsDate);

    public AgentMonthStats sumMonth(@Param("agentId") int agentId, @Param("statsMonth") String statsMonth, @Param("category") Integer category);

    public AgentTotalStats sumTotal(@Param("agentId") int agentId, @Param("category") Integer category);

    public int insert(AgentDayStats stats);

    public int update(AgentDayStats stats);
}
