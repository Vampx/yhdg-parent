package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentCompanyTotalStatsMapper extends MasterMapper {
    public int findPageCount(AgentCompanyTotalStats search);
    public List<AgentCompanyTotalStats> findPageResult(AgentCompanyTotalStats search);
}
