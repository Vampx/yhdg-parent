package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetDayDegreeStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CabinetDayDegreeStatsService extends AbstractService {
    @Autowired
    CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;

    public List<CabinetDayDegreeStats> findList(Integer agentId, String cabinetId, int offset, int limit) {
        return cabinetDayDegreeStatsMapper.findList(agentId, cabinetId, offset, limit);
    }

    public CabinetDayDegreeStats findLast(String cabinetId) {
        return cabinetDayDegreeStatsMapper.findLast(cabinetId);
    }

    public CabinetDayDegreeStats findDayDegree(String cabinetId, String statsDate) {
        return cabinetDayDegreeStatsMapper.findDayDegree(cabinetId, statsDate);
    }

    public List<CabinetDayDegreeStats> findForStats(Integer agentId, String statsDate, int offset, int limit) {
        return cabinetDayDegreeStatsMapper.findForStats(agentId,statsDate, offset, limit);
    }

    public CabinetDayDegreeStats findForAgent(Integer agentId, String statsDate, String cabinetId) {
        return cabinetDayDegreeStatsMapper.findForAgent(agentId,statsDate,cabinetId);
    }

}
