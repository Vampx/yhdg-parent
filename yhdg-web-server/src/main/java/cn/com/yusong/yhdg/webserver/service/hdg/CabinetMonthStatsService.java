package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMonthStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetMonthStatsService extends AbstractService{
    @Autowired
    CabinetMonthStatsMapper cabinetMonthStatsMapper;

    public Page findPage(CabinetMonthStats search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetMonthStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetMonthStats> cabinetMonthStatsList = cabinetMonthStatsMapper.findPageResult(search);
        for (CabinetMonthStats cabinetMonthStats : cabinetMonthStatsList) {
            if (cabinetMonthStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(cabinetMonthStats.getAgentId());
                if (agentInfo != null) {
                    cabinetMonthStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(cabinetMonthStatsList);
        return page;
    }

    public List<CabinetMonthStats> findForExcel (CabinetMonthStats search) {
        List<CabinetMonthStats> cabinetMonthStatsList = cabinetMonthStatsMapper.findPageResult(search);
        for (CabinetMonthStats cabinetMonthStats : cabinetMonthStatsList) {
            if (cabinetMonthStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(cabinetMonthStats.getAgentId());
                if (agentInfo != null) {
                    cabinetMonthStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        return cabinetMonthStatsList;
    }
}
