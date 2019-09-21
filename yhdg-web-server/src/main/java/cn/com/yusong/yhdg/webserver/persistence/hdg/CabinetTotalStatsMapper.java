package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetTotalStatsMapper extends MasterMapper {
    int findCountByCabinet(@Param("cabinetId") String cabinetId);
    public int findPageCount(CabinetTotalStats search);
    public List<CabinetTotalStats> findPageResult(CabinetTotalStats search);
}
