package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.AgentDayStatsMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.AgentTotalStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentTotalStatsService {
    @Autowired
    AgentTotalStatsMapper agentTotalStatsMapper;

    public Page findPage(AgentTotalStats search) {
        Page page = search.buildPage();
        page.setTotalItems(agentTotalStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(agentTotalStatsMapper.findPageResult(search));
        return page;
    }

    public List<AgentTotalStats> findForExcel (AgentTotalStats search) {
        return agentTotalStatsMapper.findPageResult(search);
    }
}
