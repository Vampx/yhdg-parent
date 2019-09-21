package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentPermMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.AgentPerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentPermService extends AbstractService{
    @Autowired
    AgentPermMapper agentPermMapper;

    public List<String> findAllByClientType() {
        return agentPermMapper.findAllByClientType(AgentPerm.ClientType.H5.getValue());
    }
}
