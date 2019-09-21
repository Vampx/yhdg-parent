package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformDayStatsMapper extends MasterMapper {
    public int findPageCount(PlatformDayStats search);

    public List<PlatformDayStats> findPageResult(PlatformDayStats search);

    public PlatformDayStats findByDate(@Param("statsDate") String statsDate);
}
