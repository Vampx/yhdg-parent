package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.serviceserver.persistence.basic.AgentSystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentSystemConfigService {
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;

    public String findConfigValue(int agentId, String id) {
        return agentSystemConfigMapper.findConfigValue(agentId, id);
    }

}
