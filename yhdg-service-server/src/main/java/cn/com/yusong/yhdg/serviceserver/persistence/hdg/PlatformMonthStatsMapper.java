package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PlatformMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PlatformMonthStatsMapper extends MasterMapper {
    public PlatformMonthStats find(@Param("statsMonth") String statsMonth);
    public int insert(PlatformMonthStats stats);
    public int update(PlatformMonthStats stats);
}
