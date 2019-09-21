package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PlatformDayStatsMapper extends MasterMapper {
    public PlatformDayStats find(@Param("statsDate") String statsDate);
    public PlatformMonthStats sumMonth(@Param("statsMonth") String statsMonth);
    public int insert(PlatformDayStats stats);
    public int update(PlatformDayStats stats);
}
