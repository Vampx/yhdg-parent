package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDayDegreeStatsMapper extends MasterMapper {
    int findPageCount(CabinetDayDegreeStats degreeStats);

    List<CabinetDayDegreeStats> findPageResult(CabinetDayDegreeStats degreeStats);

    CabinetDayDegreeStats findByCabinetId(@Param("cabinetId") String cabinetId);
    CabinetDayDegreeStats findLast(@Param("cabinetId") String cabinetId);
}
