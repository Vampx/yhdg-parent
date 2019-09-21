package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.AgentMonthStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgentMonthStatsService extends AbstractService {
    @Autowired
    AgentMonthStatsMapper agentMonthStatsMapper;

    public AgentMonthStats find(Integer agentId, String statsMonth, Integer category) {
        return agentMonthStatsMapper.find(agentId, statsMonth, category);
    }

    public List<AgentMonthStats> findList(Integer agentId, Integer category) {
        return agentMonthStatsMapper.findList(agentId, category);
    }

    public AgentMonthStats findTotal(Integer agentId, Integer category) {
        return agentMonthStatsMapper.findTotal(agentId, category);
    }

}
