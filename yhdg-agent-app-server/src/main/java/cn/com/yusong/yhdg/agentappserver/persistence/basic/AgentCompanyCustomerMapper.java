
package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AgentCompanyCustomerMapper extends MasterMapper {
    List<AgentCompanyCustomer> findByCompanyId(@Param("agentCompanyId") String agentCompanyId);

    List<AgentCompanyCustomer> findByCustomerId(@Param("customerId") Long customerId);

    List<AgentCompanyCustomer> findDateRange(@Param("agentId") Integer agentId, @Param("agentCompanyId") String agentCompanyId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    List<AgentCompanyCustomer> findByMobile(@Param("customerMobile") String customerMobile);

    int insert(AgentCompanyCustomer agentCompanyCustomer);

    int delete(@Param("agentCompanyId") String agentCompanyId, @Param("customerId") Long customerId);
}

