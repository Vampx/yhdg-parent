package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerWhitelistMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.PartnerMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.CustomerWhitelist;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CustomerWhitelistService extends AbstractService {
    @Autowired
    CustomerWhitelistMapper customerWhitelistMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    AgentMapper agentMapper;

    public CustomerWhitelist find(Integer id) {
        CustomerWhitelist customerWhitelist = customerWhitelistMapper.find(id);
        return customerWhitelist;
    }

    public Page findPage(CustomerWhitelist search) {
        Page page = search.buildPage();
        page.setTotalItems(customerWhitelistMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerWhitelist> customerWhitelistList = customerWhitelistMapper.findPageResult(search);
        for (CustomerWhitelist customerWhitelist : customerWhitelistList) {
            Partner partner = partnerMapper.find(customerWhitelist.getPartnerId());
            if (partner != null) {
                customerWhitelist.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(customerWhitelistList);
        return page;
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(CustomerWhitelist customerWhitelist) {
        if (customerWhitelist.getAgentId() == null) {
            customerWhitelist.setAgentId(0);
            customerWhitelist.setAgentName("无");
        } else {
            Agent agent = agentMapper.find(customerWhitelist.getAgentId());
            if (agent != null) {
                customerWhitelist.setAgentName(agent.getAgentName());
                customerWhitelist.setPartnerId(agent.getPartnerId());
            }
        }
        boolean mobile = customerWhitelistMapper.findUnique(customerWhitelist.getMobile()) == 0;
        if (!mobile) {
            return ExtResult.failResult("该手机号码已存在");
        }
        customerWhitelist.setCreateTime(new Date());
        customerWhitelistMapper.insert(customerWhitelist);
        return ExtResult.successResult();
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(CustomerWhitelist customerWhitelist) {
        int total = customerWhitelistMapper.update(customerWhitelist);
        if (total == 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("修改失败！");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(Integer id) {
        customerWhitelistMapper.delete(id);
        return ExtResult.successResult();
    }
}
