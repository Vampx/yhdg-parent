
package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyCustomerMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.management.resources.agent;

import java.util.*;


@Service
public class AgentCompanyCustomerService extends AbstractService {
    @Autowired
    AgentCompanyCustomerMapper agentCompanyCustomerMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    AgentMapper agentMapper;

    public List<AgentCompanyCustomer> findByCompanyId(String agentCompanyId) {
        return agentCompanyCustomerMapper.findByCompanyId(agentCompanyId);
    }

    public List<AgentCompanyCustomer> findByCustomerId(Long customerId) {
        return agentCompanyCustomerMapper.findByCustomerId(customerId);
    }

    public int insert(AgentCompanyCustomer agentCompanyCustomer) {
        agentCompanyCustomer.setCreateTime(new Date());
        customerMapper.bindCompany(agentCompanyCustomer.getCustomerId(), agentCompanyCustomer.getAgentCompanyId());
        return agentCompanyCustomerMapper.insert(agentCompanyCustomer);
    }

    public int delete(String agentCompanyId, Long customerId) {
        customerMapper.clearAgentCompanyId(customerId);
        return agentCompanyCustomerMapper.delete(agentCompanyId, customerId);
    }

    public List<AgentCompanyCustomer> findDateRange(Integer agentId, String agentCompanyId, Date beginTime, Date endTime) {
        return agentCompanyCustomerMapper.findDateRange(agentId, agentCompanyId, beginTime, endTime);
    }

    public List<AgentCompanyCustomer> findByMobile(String customerMobile) {
        return agentCompanyCustomerMapper.findByMobile(customerMobile);
    }
}


