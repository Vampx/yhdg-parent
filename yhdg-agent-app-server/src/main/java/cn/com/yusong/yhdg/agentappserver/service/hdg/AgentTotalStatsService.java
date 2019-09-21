package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.AgentTotalStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AgentTotalStatsService extends AbstractService {
    @Autowired
    AgentTotalStatsMapper agentTotalStatsMapper;

    public AgentTotalStats find(Integer agentId, Integer category) {
        return agentTotalStatsMapper.find(agentId, category);
    }

}
