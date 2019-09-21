package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface CabinetBatteryStatsMapper extends MasterMapper {

    CabinetBatteryStats find(long id);

    int findPageCount(CabinetBatteryStats search);

    List<CabinetBatteryStats> findPageResult(CabinetBatteryStats search);
}
