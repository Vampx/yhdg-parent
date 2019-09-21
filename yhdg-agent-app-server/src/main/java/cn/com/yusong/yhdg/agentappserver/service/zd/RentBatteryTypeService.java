package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentBatteryTypeMapper;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RentBatteryTypeService extends AbstractService {

    @Autowired
    RentBatteryTypeMapper rentBatteryTypeMapper;

    public RentBatteryType find(int batteryType, int agentId) {
        return rentBatteryTypeMapper.find(batteryType, agentId);
    }

    public List<RentBatteryType> findListByAgentId(int agentId) {
        return rentBatteryTypeMapper.findListByAgentId(agentId);
    }
}
