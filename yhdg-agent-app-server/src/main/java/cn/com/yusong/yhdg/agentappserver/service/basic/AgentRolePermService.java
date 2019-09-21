package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentRolePermMapper;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgentRolePermService extends AbstractService {

    @Autowired
    AgentRolePermMapper agentRolePermMapper;

    public List<String> findAll(Integer roleId) {
        return agentRolePermMapper.findAll(roleId);
    }
}
