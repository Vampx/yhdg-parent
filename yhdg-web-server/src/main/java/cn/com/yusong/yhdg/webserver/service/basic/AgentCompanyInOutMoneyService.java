package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyInOutMoney;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentCompanyInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCompanyInOutMoneyService extends AbstractService{

    @Autowired
    AgentCompanyInOutMoneyMapper agentCompanyInOutMoneyMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;


    public Page findPage(AgentCompanyInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(agentCompanyInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentCompanyInOutMoney> pageResult = agentCompanyInOutMoneyMapper.findPageResult(search);
        for (AgentCompanyInOutMoney agentCompanyInOutMoney : pageResult) {
            if (agentCompanyInOutMoney.getAgentCompanyId() != null) {
                AgentCompany agentCompany = agentCompanyMapper.find(agentCompanyInOutMoney.getAgentCompanyId());
                if (agentCompany != null) {
                    agentCompanyInOutMoney.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
        }
        page.setResult(pageResult);
        return page;
    }

    public AgentCompanyInOutMoney find(Long id) {
        AgentCompanyInOutMoney agentCompanyInOutMoney = agentCompanyInOutMoneyMapper.find(id);
        if (agentCompanyInOutMoney.getAgentCompanyId() != null) {
            AgentCompany agentCompany = agentCompanyMapper.find(agentCompanyInOutMoney.getAgentCompanyId());
            if (agentCompany != null) {
                agentCompanyInOutMoney.setAgentId(agentCompany.getAgentId());
            }
        }
        return agentCompanyInOutMoney;
    }
}
