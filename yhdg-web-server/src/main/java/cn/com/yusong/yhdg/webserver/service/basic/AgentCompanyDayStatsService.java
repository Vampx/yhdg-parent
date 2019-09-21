package cn.com.yusong.yhdg.webserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentCompanyDayStatsMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopDayStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCompanyDayStatsService extends AbstractService{
    @Autowired
    AgentCompanyDayStatsMapper agentCompanyDayStatsMapper;

    public Page findPage(AgentCompanyDayStats search) {
        Page page = search.buildPage();
        page.setTotalItems(agentCompanyDayStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentCompanyDayStats> agentCompanyDayStatsList = agentCompanyDayStatsMapper.findPageResult(search);
        for (AgentCompanyDayStats agentCompanyDayStats : agentCompanyDayStatsList) {
            if (agentCompanyDayStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(agentCompanyDayStats.getAgentId());
                if (agentInfo != null) {
                    agentCompanyDayStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(agentCompanyDayStatsList);
        return page;
    }

    public List<AgentCompanyDayStats> findForExcel (AgentCompanyDayStats search) {
        return agentCompanyDayStatsMapper.findPageResult(search);
    }
}
