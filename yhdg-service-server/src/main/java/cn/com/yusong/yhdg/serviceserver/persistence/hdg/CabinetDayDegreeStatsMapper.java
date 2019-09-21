package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CabinetDayDegreeStatsMapper extends MasterMapper {
    public int findUseVolume(@Param("cabinetId") String cabinetId);

    public List<CabinetDayStats> findIncrement(@Param("statsDate") String statsDate);

    public List<CabinetDayDegreeStats> findListByAgent(@Param("agentId") int agentId, @Param("statsDate") String statsDate, @Param("offset") int offset, @Param("limit") int limit);

}
