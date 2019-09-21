package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.hdg.IncomeRatioHistory;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.AgentCompanyCustomerMapper;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.AgentCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentCompanyCustomerService {
    @Autowired
    AgentCompanyCustomerMapper agentCompanyCustomerMapper;

    public AgentCompanyCustomer findByCompanyMobile(String agentCompanyId, String customerMobile) {
        return agentCompanyCustomerMapper.findByCompanyMobile(agentCompanyId, customerMobile);
    }

    public int insert(AgentCompanyCustomer agentCompanyCustomer) {
        return agentCompanyCustomerMapper.insert(agentCompanyCustomer);
    }
}
