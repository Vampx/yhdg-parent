package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.AgentSystemConfigMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentSystemConfigService{
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;

    public String findConfigValue(String id, Integer agentId) {
        return agentSystemConfigMapper.findConfigValue( id, agentId);
    }

}
