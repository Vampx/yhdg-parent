package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ShopDayStatsMapper extends MasterMapper {
    public int findPageCount(ShopDayStats search);
    public List<ShopDayStats> findPageResult(ShopDayStats search);
}
