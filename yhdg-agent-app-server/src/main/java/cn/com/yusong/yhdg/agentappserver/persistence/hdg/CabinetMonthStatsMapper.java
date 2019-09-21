package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetMonthStatsMapper extends MasterMapper {
    List<CabinetMonthStats> findByCabinetList(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId);

    CabinetMonthStats findTotal(@Param("cabinetId") String cabinetId);
}
