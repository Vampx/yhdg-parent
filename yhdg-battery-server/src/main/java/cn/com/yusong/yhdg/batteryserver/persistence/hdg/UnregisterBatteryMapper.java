package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface UnregisterBatteryMapper extends MasterMapper {
    UnregisterBattery find(String id);

    int insert(UnregisterBattery unregisterBattery);

    int update(UnregisterBattery unregisterBattery);
}
