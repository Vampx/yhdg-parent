package cn.com.yusong.yhdg.webserver.persistence.basic;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyCustomerMapper extends MasterMapper {

	int findPageCount(AgentCompanyCustomer agentCompanyCustomer);

	List<AgentCompanyCustomer> findPageResult(AgentCompanyCustomer agentCompanyCustomer);

	List<AgentCompanyCustomer> findByCompanyId(@Param("agentCompanyId") String agentCompanyId);

	int insert(AgentCompanyCustomer agentCompanyCustomer);

	int delete(@Param("agentCompanyId") String agentCompanyId, @Param("customerId") Long customerId);
}
