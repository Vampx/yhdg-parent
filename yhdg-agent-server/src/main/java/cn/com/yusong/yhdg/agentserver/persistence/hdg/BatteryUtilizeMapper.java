package cn.com.yusong.yhdg.agentserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.BatteryUtilize;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface BatteryUtilizeMapper extends MasterMapper {

    int findPageCount(BatteryUtilize battery);

    List<BatteryUtilize> findPageResult(BatteryUtilize battery);
}
