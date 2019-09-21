package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentCompanyCustomerMapper extends MasterMapper {
    AgentCompanyCustomer findByCompanyMobile(@Param("agentCompanyId") String agentCompanyId, @Param("customerMobile") String customerMobile);

    int insert(AgentCompanyCustomer agentCompanyCustomer);
}
