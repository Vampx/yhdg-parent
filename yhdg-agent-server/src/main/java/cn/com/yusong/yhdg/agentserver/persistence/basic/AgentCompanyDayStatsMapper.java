package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentCompanyDayStatsMapper extends MasterMapper {
    public int findPageCount(AgentCompanyDayStats search);
    public List<AgentCompanyDayStats> findPageResult(AgentCompanyDayStats search);
}
