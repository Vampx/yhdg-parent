package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentRolePermMapper extends MasterMapper {
    List<String> findAll(Integer roleId);
}
