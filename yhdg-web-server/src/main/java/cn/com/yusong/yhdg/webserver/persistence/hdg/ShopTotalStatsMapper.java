package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ShopTotalStatsMapper extends MasterMapper {
    public int findPageCount(ShopTotalStats search);
    public List<ShopTotalStats> findPageResult(ShopTotalStats search);
}
