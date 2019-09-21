package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentRolePerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentRolePermMapper extends MasterMapper {
    List<String> findAll(@Param("roleId") Integer roleId);
    int insert(AgentRolePerm agentRolePerm);
    int delete(int roleId);
}
