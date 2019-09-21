package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.AgentDayInOutMoney;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AgentDayInOutMoneyService extends AbstractService{
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    AgentDayInOutMoneyMapper agentDayInOutMoneyMapper;

    @Autowired
    AgentMapper agentMapper;

    public void stats(Date date) {
        String statsDate = DateFormatUtils.format(date.getTime(), Constant.DATE_FORMAT);
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);

        //查询运营商收入支出统计
        List<AgentInOutMoney> agentInOutMoneyList = agentInOutMoneyMapper.findByAgent(beginTime, endTime);

        //运营商当日统计map
        Map<Integer, AgentDayInOutMoney> agentDayInOutMoneyMap = new HashMap<Integer, AgentDayInOutMoney>();

        //运营商收入支出统计
        for (AgentInOutMoney e : agentInOutMoneyList) {
            AgentDayInOutMoney agentDayInOutMoney = agentDayInOutMoneyMap.get(e.getAgentId());
            if (agentDayInOutMoney == null) {
                agentDayInOutMoney = new AgentDayInOutMoney();
                agentDayInOutMoney.init();
                agentDayInOutMoney.setAgentId(e.getAgentId());
                agentDayInOutMoneyMap.put(agentDayInOutMoney.getAgentId(), agentDayInOutMoney);
            }
            if(e.getType() == AgentInOutMoney.Type.IN.getValue()){
                agentDayInOutMoney.setAgentInMoney(e.getMoney());
            }else if(e.getType() == AgentInOutMoney.Type.OUT.getValue()){
                agentDayInOutMoney.setAgentOutMoney(e.getMoney());
            }
        }


        //统计保存
        for (AgentDayInOutMoney e : agentDayInOutMoneyMap.values()) {
            e.setStatsDate(statsDate);
            Agent agent = agentMapper.find(e.getAgentId());
            e.setAgentId(agent.getId());
            e.setAgentName(agent.getAgentName());
            AgentDayInOutMoney agentDayInOutMoney = agentDayInOutMoneyMapper.find(e.getAgentId(), e.getStatsDate());
            if (agentDayInOutMoney == null) {
                agentDayInOutMoneyMapper.insert(e);
            } else {
                agentDayInOutMoneyMapper.update(e);
            }
        }

    }

}
