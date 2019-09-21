package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDayStatsMapper extends MasterMapper {
    int findCountByCabinet(@Param("cabinetId") String cabinetId);
    public int findPageCount(CabinetDayStats search);
    public List<CabinetDayStats> findPageResult(CabinetDayStats search);
}
