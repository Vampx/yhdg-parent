package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.CustomerExchangeInfoMapper;
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
