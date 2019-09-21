package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyRolePerm;
import cn.com.yusong.yhdg.common.domain.basic.ShopRolePerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyRolePermMapper extends MasterMapper {
	List<String> findAll(@Param("roleId") Integer roleId);
	int insert(AgentCompanyRolePerm agentCompanyRolePerm);
	int delete(@Param("roleId") Integer roleId);
}
