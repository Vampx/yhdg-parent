package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyTotalStatsMapper extends MasterMapper {
	List<AgentCompanyTotalStats> findList(@Param("agentId") int agentId,
									  @Param("agentCompanyId") String agentCompanyId,
									  @Param("category") Integer category);
}
