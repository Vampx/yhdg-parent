package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetMonthStatsMapper extends MasterMapper {
    public CabinetMonthStats find(@Param("cabinetId") String cabinetId, @Param("agentId") int agentId, @Param("statsMonth") String statsMonth);
    public int insert(CabinetMonthStats stats);
    public int update(CabinetMonthStats stats);
}
