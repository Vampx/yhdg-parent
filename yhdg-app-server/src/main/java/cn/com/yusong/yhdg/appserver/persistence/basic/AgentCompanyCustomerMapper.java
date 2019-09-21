package cn.com.yusong.yhdg.appserver.persistence.basic;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AgentCompanyCustomerMapper extends MasterMapper {

	AgentCompanyCustomer findByCustomerId(@Param("customerId") Long customerId);

	List<AgentCompanyCustomer> findByMobile(@Param("customerMobile") String customerMobile);

	int insert(AgentCompanyCustomer agentCompanyCustomer);

}
