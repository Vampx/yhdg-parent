package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.EstateInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AgentCompanyInOutMoneyMapper extends MasterMapper {
    List<AgentCompanyInOutMoney> findList(@Param("agentCompanyId") String agentCompanyId,
										  @Param("offset") int offset,
										  @Param("limit") int limit);
    List<AgentCompanyInOutMoney> findIncome(@Param("agentCompanyId") String agentCompanyId,
									  @Param("type") int type,
									  @Param("offset") int offset,
									  @Param("limit") int limit);
    int findIncomeCount(@Param("agentCompanyId") String agentCompanyId, @Param("type") int type);
    int insert(AgentCompanyInOutMoney agentCompanyInOutMoney);
}
