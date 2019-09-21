package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyDayStatsMapper extends MasterMapper {
	List<AgentCompanyDayStats> findList(@Param("agentId") int agentId,
					  @Param("agentCompanyId") String agentCompanyId,
					  @Param("statsDate") String statsDate,
					  @Param("category") Integer category);

	List<AgentCompanyDayStats> findByCompanyId(@Param("agentCompanyId") String agentCompanyId);

	List<AgentCompanyDayStats> findDateRange(@Param("agentId") int agentId,
									 @Param("agentCompanyId") String agentCompanyId,
									 @Param("beginDate") String beginDate,
									 @Param("endDate") String endDate,
									 @Param("category") Integer category);
}
