package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentDayStatsMapper extends MasterMapper {
    AgentDayStats find(@Param("agentId") int agentId, @Param("statsDate") String statsDate, @Param("category") Integer category);
    AgentDayStats findAgentDayTotal(@Param("agentId") int agentId,
                                    @Param("beginDate") String beginDate,
                                    @Param("endDate") String endDate,
                                    @Param("category") Integer category);


}
