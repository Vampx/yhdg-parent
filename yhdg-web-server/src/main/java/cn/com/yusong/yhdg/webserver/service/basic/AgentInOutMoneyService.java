package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentInOutMoneyService extends AbstractService{

    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;

    public AgentInOutMoney find(Long id) {
        return agentInOutMoneyMapper.find(id);
    }

    public Page findPage(AgentInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(agentInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentInOutMoney> pageResult = agentInOutMoneyMapper.findPageResult(search);
        for (AgentInOutMoney agentInOutMoney : pageResult) {
            if (agentInOutMoney.getAgentId() != null) {
                agentInOutMoney.setAgentName(findAgentInfo(agentInOutMoney.getAgentId()).getAgentName());
            }
        }
        page.setResult(pageResult);
        return page;
    }

    public List<AgentInOutMoney> findForExcel(AgentInOutMoney search) {
        List<AgentInOutMoney> pageResult = agentInOutMoneyMapper.findPageResult(search);
        for (AgentInOutMoney agentInOutMoney : pageResult) {
            if (agentInOutMoney.getAgentId() != null) {
                agentInOutMoney.setAgentName(findAgentInfo(agentInOutMoney.getAgentId()).getAgentName());
            }
        }
        return pageResult;
    }

}
