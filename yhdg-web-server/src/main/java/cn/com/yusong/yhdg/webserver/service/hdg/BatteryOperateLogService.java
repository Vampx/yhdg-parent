package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryOperateLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteryOperateLogService {
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;

    public List<BatteryOperateLog> findList(Long id, String batteryId) {
        List<BatteryOperateLog> batteryOperateLogs = batteryOperateLogMapper.findList(id, batteryId);
        for (BatteryOperateLog batteryOperateLog: batteryOperateLogs) {
            if (batteryOperateLog.getOperateType() != null) {
                batteryOperateLog.setOperateTypeName(BatteryOperateLog.OperateType.getName(batteryOperateLog.getOperateType()));
            }
        }
        return batteryOperateLogs;
    }
}
