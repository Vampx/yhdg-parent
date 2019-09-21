package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.AgentCompanyCustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AgentCompanyService {

    @Autowired
    AgentCompanyMapper agentCompanyMapper;

    public AgentCompany find(String id) {
        return agentCompanyMapper.find(id);
    }

}
