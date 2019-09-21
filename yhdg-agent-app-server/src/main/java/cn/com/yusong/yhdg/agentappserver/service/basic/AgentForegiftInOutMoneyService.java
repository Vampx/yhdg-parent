package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentForegiftInOutMoneyMapper;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftInOutMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentForegiftInOutMoneyService {
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;


    public List<AgentForegiftInOutMoney> findList(int agentId, int category,  List<Integer> bizTypeList, Integer offset, Integer limit) {
        return agentForegiftInOutMoneyMapper.findList(agentId, category, bizTypeList, offset, limit);
    }
}
