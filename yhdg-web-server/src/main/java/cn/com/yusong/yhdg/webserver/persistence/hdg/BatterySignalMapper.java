package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatterySignal;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface BatterySignalMapper extends MasterMapper {

    int findPageCount(BatterySignal batterySignal);

    List<BatterySignal> findPageResult(BatterySignal batterySignal);
}
