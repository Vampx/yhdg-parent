package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.AgentMaterialDayStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AgentMaterialDayStatsService extends AbstractService {
    @Autowired
    AgentMaterialDayStatsMapper agentMaterialDayStatsMapper;

    public AgentMaterialDayStats find(Integer agentId, String statsDate, Integer category) {
        return agentMaterialDayStatsMapper.find(agentId, statsDate, category);
    }

    public int findCountByStatus(Integer agentId, Integer status) {
        return agentMaterialDayStatsMapper.findCountByStatus(agentId, status);
    }

    public AgentMaterialDayStats findTotalMoney(Integer agentId, Integer category) {
        return agentMaterialDayStatsMapper.findTotalMoney(agentId, category);
    }

    public AgentMaterialDayStats findTotalRentMoney(Integer agentId, String beginDate, String endDate, Integer category) {
        return agentMaterialDayStatsMapper.findTotalRentMoney(agentId, beginDate, endDate, category);
    }
}
