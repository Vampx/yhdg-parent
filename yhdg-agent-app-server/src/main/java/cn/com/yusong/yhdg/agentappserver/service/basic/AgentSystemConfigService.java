package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentSystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AgentSystemConfigService {
    @Autowired
    AgentSystemConfigMapper systemConfigMapper;

    public String findConfigValue(String id, Integer agentId) {
        return systemConfigMapper.findConfigValue(id, agentId);
    }

}
