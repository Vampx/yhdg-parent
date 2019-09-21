package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetTotalStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetTotalStatsService extends AbstractService{
    @Autowired
    CabinetTotalStatsMapper cabinetTotalStatsMapper;

    public Page findPage(CabinetTotalStats search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetTotalStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetTotalStats> cabinetTotalStatsList = cabinetTotalStatsMapper.findPageResult(search);
        for (CabinetTotalStats cabinetTotalStats : cabinetTotalStatsList) {
            if (cabinetTotalStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(cabinetTotalStats.getAgentId());
                if (agentInfo != null) {
                    cabinetTotalStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(cabinetTotalStatsList);

        return page;
    }

   public List<CabinetTotalStats> findForExcel (CabinetTotalStats search) {
        List<CabinetTotalStats> cabinetTotalStatsList = cabinetTotalStatsMapper.findPageResult(search);
        for (CabinetTotalStats cabinetTotalStats : cabinetTotalStatsList) {
            if (cabinetTotalStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(cabinetTotalStats.getAgentId());
                if (agentInfo != null) {
                    cabinetTotalStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        return cabinetTotalStatsList;
    }
}
