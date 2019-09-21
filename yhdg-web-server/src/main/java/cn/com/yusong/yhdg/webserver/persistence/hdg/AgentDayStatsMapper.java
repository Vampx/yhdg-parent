package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentDayStatsMapper extends MasterMapper {
    public int findPageCount(AgentDayStats search);
    public List<AgentDayStats> findPageResult(AgentDayStats search);
}
