package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyRole;
import cn.com.yusong.yhdg.common.domain.basic.ShopRole;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyRoleMapper extends MasterMapper {
	public AgentCompanyRole find(int id);
	int findUnique(@Param("id") Integer id, @Param("roleName") String roleName);
	public List<AgentCompanyRole> findByAgentCompanyId(@Param("agentCompanyId") String shopId);
	public int findPageCount(AgentCompanyRole search);
	public List<AgentCompanyRole> findPageResult(AgentCompanyRole search);
	public int insert(AgentCompanyRole role);
	public int update(AgentCompanyRole role);
	public int delete(int id);
}
