package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.FaultLogMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaultLogService extends AbstractService {

    @Autowired
    FaultLogMapper faultLogMapper;

    public List<FaultLog> findByBatteryId(int agentId, String batteryId,int offset, int limit) {
        return faultLogMapper.findByBatteryId(agentId, batteryId, offset, limit);
    }

    public List<FaultLog> findByCabinetId(int agentId, String cabinetId,int offset, int limit) {
        return faultLogMapper.findByCabinetId(agentId, cabinetId, offset, limit);
    }
}
