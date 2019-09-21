
package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyIncomeRatioHistory;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;


public interface AgentCompanyIncomeRatioHistoryMapper extends HistoryMapper {
	AgentCompanyIncomeRatioHistory find(@Param("statsDate") String statsDate, @Param("agentId") Integer agentId, @Param("agentCompanyId") String agentCompanyId, @Param("orgType") Integer orgType, @Param("suffix") String suffix);

	String exist(@Param("suffix") String suffix);
}

