package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyPerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentCompanyPermMapper extends MasterMapper {
	public List<AgentCompanyPerm> findAll();
}
