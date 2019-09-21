package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDayStatsMapper extends MasterMapper {
    public CabinetDayStats find(@Param("cabinetId") String cabinetId, @Param("agentId") int agentId, @Param("statsDate") String statsDate);
    public CabinetMonthStats sumMonth(@Param("cabinetId") String cabinetId, @Param("agentId") int agentId, @Param("statsMonth") String statsMonth);
    public CabinetTotalStats sumTotal(@Param("cabinetId") String cabinetId, @Param("agentId") int agentId);
    public int perElectric(@Param("cabinetId") String cabinetId, @Param("agentId") int agentId);
    public int insert(CabinetDayStats stats);
    public int update(CabinetDayStats stats);
}
