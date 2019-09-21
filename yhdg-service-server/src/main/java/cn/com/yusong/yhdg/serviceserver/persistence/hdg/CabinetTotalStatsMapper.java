package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetTotalStatsMapper extends MasterMapper {
    public CabinetTotalStats find(@Param("cabinetId") String cabinetId, @Param("agentId") int agentId);
    public int perElectric(@Param("agentId") int agentId);
    public int insert(CabinetTotalStats stats);
    public int update(CabinetTotalStats stats);
}
