package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentRolePerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentRolePermMapper extends MasterMapper {
    List<String> findAll(Integer roleId);
    int insert(AgentRolePerm agentRolePerm);
    int delete(int roleId);
}
