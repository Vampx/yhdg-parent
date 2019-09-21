package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetMonthStatsMapper extends MasterMapper {
    int findCountByCabinet(@Param("cabinetId") String cabinetId);
    public int findPageCount(CabinetMonthStats search);
    public List<CabinetMonthStats> findPageResult(CabinetMonthStats search);
}
