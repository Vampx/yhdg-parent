package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetDayStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetDayStatsService extends AbstractService{
    @Autowired
    CabinetDayStatsMapper cabinetDayStatsMapper;

    public Page findPage(CabinetDayStats search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetDayStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetDayStats> cabinetDayStatsList = cabinetDayStatsMapper.findPageResult(search);
        for (CabinetDayStats cabinetDayStats : cabinetDayStatsList) {
            if (cabinetDayStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(cabinetDayStats.getAgentId());
                if (agentInfo != null) {
                    cabinetDayStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(cabinetDayStatsList);

        return page;
    }

    public List<CabinetDayStats> findForExcel (CabinetDayStats search) {
        List<CabinetDayStats> cabinetDayStatsList = cabinetDayStatsMapper.findPageResult(search);
        for (CabinetDayStats cabinetDayStats : cabinetDayStatsList) {
            if (cabinetDayStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(cabinetDayStats.getAgentId());
                if (agentInfo != null) {
                    cabinetDayStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        return cabinetDayStatsList;
    }
}
