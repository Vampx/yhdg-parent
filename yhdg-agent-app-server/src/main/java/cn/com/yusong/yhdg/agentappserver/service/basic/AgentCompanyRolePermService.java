package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyRolePermMapper;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgentCompanyRolePermService extends AbstractService {

    @Autowired
    AgentCompanyRolePermMapper agentCompanyRolePermMapper;

    public List<String> findAgentCompanyRoleAll(Integer agentCompanyRoleId) {
        return agentCompanyRolePermMapper.findAgentCompanyRoleAll(agentCompanyRoleId);
    }
}
