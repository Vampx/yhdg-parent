package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.staticserver.persistence.basic.AgentSystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentSystemConfigService extends AbstractService {
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;

    public String findConfigValue(int agentId, String id) {
        return findAgentConfigValue(agentId, id);
    }

}
