package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PlatformMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface PlatformMonthStatsMapper extends MasterMapper {
    public int findPageCount(PlatformMonthStats search);
    public List<PlatformMonthStats> findPageResult(PlatformMonthStats search);
}
