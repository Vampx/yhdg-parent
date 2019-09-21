package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentForegiftInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentForegiftInOutMoneyService extends AbstractService{

    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;

    public Page findPage(AgentForegiftInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(agentForegiftInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentForegiftInOutMoney> pageResult = agentForegiftInOutMoneyMapper.findPageResult(search);
        for (AgentForegiftInOutMoney agentInOutMoney : pageResult) {
            if (agentInOutMoney.getAgentId() != null) {
                agentInOutMoney.setAgentName(findAgentInfo(agentInOutMoney.getAgentId()).getAgentName());
            }
        }
        page.setResult(pageResult);
        return page;
    }

}
