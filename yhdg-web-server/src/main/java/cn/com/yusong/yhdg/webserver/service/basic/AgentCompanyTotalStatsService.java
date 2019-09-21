package cn.com.yusong.yhdg.webserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyTotalStats;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentCompanyTotalStatsMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopTotalStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCompanyTotalStatsService extends AbstractService{
    @Autowired
    AgentCompanyTotalStatsMapper agentCompanyTotalStatsMapper;

    public Page findPage(AgentCompanyTotalStats search) {
        Page page = search.buildPage();
        page.setTotalItems(agentCompanyTotalStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentCompanyTotalStats> agentCompanyTotalStatsList = agentCompanyTotalStatsMapper.findPageResult(search);
        for (AgentCompanyTotalStats agentCompanyTotalStats : agentCompanyTotalStatsList) {
            if (agentCompanyTotalStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(agentCompanyTotalStats.getAgentId());
                if (agentInfo != null) {
                    agentCompanyTotalStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(agentCompanyTotalStatsList);
        return page;
    }

    public List<AgentCompanyTotalStats> findForExcel (AgentCompanyTotalStats search) {
        return agentCompanyTotalStatsMapper.findPageResult(search);
    }
}
