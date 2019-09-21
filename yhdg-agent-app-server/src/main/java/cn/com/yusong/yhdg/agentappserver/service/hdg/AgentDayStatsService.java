package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.AgentDayStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgentDayStatsService extends AbstractService {
    @Autowired
    AgentDayStatsMapper agentDayStatsMapper;

    public AgentDayStats find(Integer agentId, String statsDate, Integer category) {
        return agentDayStatsMapper.find(agentId,statsDate, category);
    }

    public AgentDayStats findAgentDayTotal(Integer agentId, String beginDate, String endDate, Integer category) {
        return agentDayStatsMapper.findAgentDayTotal(agentId, beginDate, endDate, category);
    }

}
