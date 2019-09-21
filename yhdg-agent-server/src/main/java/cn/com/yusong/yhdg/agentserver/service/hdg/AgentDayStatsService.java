package cn.com.yusong.yhdg.agentserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.AgentDayStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentDayStatsService {
    @Autowired
    AgentDayStatsMapper agentDayStatsMapper;

    public Page findPage(AgentDayStats search) {
        Page page = search.buildPage();
        page.setTotalItems(agentDayStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(agentDayStatsMapper.findPageResult(search));
        return page;
    }

    public List<AgentDayStats> findForExcel (AgentDayStats search) {
        return agentDayStatsMapper.findPageResult(search);
    }

    public AgentDayStats findByDate(int agentId,String statsDate) {
        return agentDayStatsMapper.findByDate(agentId,statsDate);
    }
}
