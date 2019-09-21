package cn.com.yusong.yhdg.agentserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.AgentMonthStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentMonthStatsService {
    @Autowired
    AgentMonthStatsMapper agentMonthStatsMapper;

    public Page findPage(AgentMonthStats search) {
        Page page = search.buildPage();
        page.setTotalItems(agentMonthStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(agentMonthStatsMapper.findPageResult(search));
        return page;
    }

    public List<AgentMonthStats> findForExcel (AgentMonthStats search) {
        return agentMonthStatsMapper.findPageResult(search);
    }
}
