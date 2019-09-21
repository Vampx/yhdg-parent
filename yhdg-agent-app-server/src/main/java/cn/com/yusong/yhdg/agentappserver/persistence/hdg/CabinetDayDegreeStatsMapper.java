package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CabinetDayDegreeStatsMapper extends MasterMapper {
    List<CabinetDayDegreeStats> findList(@Param("agentId") int agentId,
                                         @Param("cabinetId") String cabinetId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    CabinetDayDegreeStats findLast(@Param("cabinetId") String cabinetId);

    CabinetDayDegreeStats findDayDegree(@Param("cabinetId") String cabinetId, @Param("statsDate") String statsDate);

    List<CabinetDayDegreeStats> findForStats(@Param("agentId") int agentId, @Param("statsDate") String statsDate, @Param("offset") int offset,
                                             @Param("limit") int limit);

    CabinetDayDegreeStats findForAgent(@Param("agentId") Integer agentId, @Param("statsDate") String statsDate,
                                       @Param("cabinetId") String cabinetId);
}
