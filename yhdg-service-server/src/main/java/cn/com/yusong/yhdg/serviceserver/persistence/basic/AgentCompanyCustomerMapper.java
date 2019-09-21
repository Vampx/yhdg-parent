package cn.com.yusong.yhdg.serviceserver.persistence.basic;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyIncomeRatioHistory;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyCustomerMapper extends MasterMapper {
    public AgentCompanyCustomer find(@Param("agentCompanyId") String agentCompanyId, @Param("customerId") Long customerId);
}
