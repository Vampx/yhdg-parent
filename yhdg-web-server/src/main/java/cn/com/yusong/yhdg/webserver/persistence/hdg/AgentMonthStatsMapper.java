package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentMonthStatsMapper extends MasterMapper {
    public int findPageCount(AgentMonthStats search);
    public List<AgentMonthStats> findPageResult(AgentMonthStats search);
}
