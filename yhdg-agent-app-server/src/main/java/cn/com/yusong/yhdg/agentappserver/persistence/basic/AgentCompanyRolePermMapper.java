package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyRolePermMapper extends MasterMapper {
    List<String> findAgentCompanyRoleAll(@Param("agentCompanyRoleId") Integer agentCompanyRoleId);
}
