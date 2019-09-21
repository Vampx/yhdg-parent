package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentMaterialDayStatsMapper extends MasterMapper {
    public AgentMaterialDayStats find(@Param("agentId") int agentId, @Param("statsDate") String statsDate, @Param("category") Integer category);
    public AgentMaterialDayStats sumMonth(@Param("agentId") int agentId, @Param("statsMonth") String statsMonth, @Param("category") Integer category);
    public AgentMaterialDayStats sumTotal(@Param("agentId") int agentId, @Param("category") Integer category);
    public int insert(AgentMaterialDayStats stats);
    public int update(AgentMaterialDayStats stats);
}
