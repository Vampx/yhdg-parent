package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgentBatteryTypeService extends AbstractService {

    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;

    public AgentBatteryType find(int batteryType, int agentId) {
        return agentBatteryTypeMapper.find(batteryType, agentId);
    }

    public List<AgentBatteryType> findListByAgentId(int agentId) {
        return agentBatteryTypeMapper.findListByAgentId(agentId);
    }
}
