package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentSettlement;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentSettlementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentSettlementService {

    @Autowired
    AgentSettlementMapper agentSettlementMapper;

    public AgentSettlement find(int id) {
        return agentSettlementMapper.find(id);
    }

    public Page findPage(AgentSettlement agentSettlement) {
        Page page = agentSettlement.buildPage();
        page.setTotalItems(agentSettlementMapper.findPageCount(agentSettlement));
        agentSettlement.setBeginIndex(page.getOffset());
        page.setResult(agentSettlementMapper.findPageResult(agentSettlement));
        return page;
    }
    public ExtResult update(int id,String operator){
        AgentSettlement agentSettlement = agentSettlementMapper.find(id);
        if (agentSettlement==null){
            return ExtResult.failResult("记录不存在");
        }
        if (!agentSettlement.getState().equals(AgentSettlement.State.AUDITING.getValue())){
            return ExtResult.failResult("结算记录未生成,不可审核");
        }
        agentSettlement.setOperator(operator);
//        agentSettlement.sht(operator);
        agentSettlement.setState(AgentSettlement.State.COMPLETE.getValue());
        return null;
    }
}