package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface BatteryOperateLogMapper extends MasterMapper {
    public int insert(BatteryOperateLog batteryOperateLog);
}
