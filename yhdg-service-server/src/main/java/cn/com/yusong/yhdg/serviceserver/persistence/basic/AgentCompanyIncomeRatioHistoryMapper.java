package cn.com.yusong.yhdg.serviceserver.persistence.basic;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyIncomeRatioHistory;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyIncomeRatioHistoryMapper extends HistoryMapper {
    public String exist(@Param("suffix") String suffix);
    public int createTable(@Param("suffix") String suffix);
    public List<AgentCompanyIncomeRatioHistory> findList(@Param("agentId") Integer agentId, @Param("agentCompanyId") String agentCompanyId, @Param("statsDate") String statsDate, @Param("suffix") String suffix);
    public AgentCompanyIncomeRatioHistory find(@Param("agentId") Integer agentId, @Param("agentCompanyId") String agentCompanyId, @Param("statsDate") String statsDate, @Param("orgType") Integer orgType, @Param("suffix") String suffix);
    public int insert(AgentCompanyIncomeRatioHistory history);
}
