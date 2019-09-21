package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyTotalStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentCompanyTotalStatsMapper extends MasterMapper {
    public AgentCompanyTotalStats find(@Param("agentCompanyId") String shopId, @Param("category") Integer category);
    public int insert(AgentCompanyTotalStats stats);
    public int update(AgentCompanyTotalStats stats);
}
