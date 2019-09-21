package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyTotalStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentCompanyDayStatsMapper extends MasterMapper {
    public AgentCompanyDayStats find(@Param("agentCompanyId") String agentCompanyId, @Param("statsDate") String statsDate, @Param("category") Integer category);
    public AgentCompanyTotalStats sumTotal(@Param("agentCompanyId") String agentCompanyId, @Param("category") Integer category);
    public int insert(AgentCompanyDayStats stats);
    public int update(AgentCompanyDayStats stats);
}
