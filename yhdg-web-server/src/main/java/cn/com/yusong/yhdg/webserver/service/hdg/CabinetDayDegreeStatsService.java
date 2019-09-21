package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetDayDegreeStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetDayDegreeStatsService extends AbstractService{
    @Autowired
    CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;
    @Autowired
    AgentMapper agentMapper;

    public Page findPage(CabinetDayDegreeStats entity) {
        Page page = entity.buildPage();
        page.setTotalItems(cabinetDayDegreeStatsMapper.findPageCount(entity));
        entity.setBeginIndex(page.getOffset());
        List<CabinetDayDegreeStats> list = cabinetDayDegreeStatsMapper.findPageResult(entity);
        for (CabinetDayDegreeStats dayDegreeStats : list) {
            if (dayDegreeStats.getAgentId() != null) {
                dayDegreeStats.setAgentName(agentMapper.find(dayDegreeStats.getAgentId()).getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }

    public CabinetDayDegreeStats findByCabinetId(String cabinetId) {
        return cabinetDayDegreeStatsMapper.findByCabinetId(cabinetId);
    }

    public CabinetDayDegreeStats findLast(String cabinetId) {
        return cabinetDayDegreeStatsMapper.findLast(cabinetId);
    }
}