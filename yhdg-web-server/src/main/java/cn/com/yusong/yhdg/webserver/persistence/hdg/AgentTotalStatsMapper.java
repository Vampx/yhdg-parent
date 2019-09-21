package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentTotalStatsMapper extends MasterMapper {
    public int findPageCount(AgentTotalStats search);
    public List<AgentTotalStats> findPageResult(AgentTotalStats search);
}
