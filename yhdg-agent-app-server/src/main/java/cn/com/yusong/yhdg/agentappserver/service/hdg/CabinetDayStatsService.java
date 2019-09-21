package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetDayStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CabinetDayStatsService extends AbstractService {
    @Autowired
    CabinetDayStatsMapper cabinetDayStatsMapper;

    public List<CabinetDayStats> findByCabinetList(Integer agentId, String cabinetId, String beginDate, String endDate, String keyword, Integer offset, Integer limit) {
        return cabinetDayStatsMapper.findByCabinetList(agentId, cabinetId, beginDate, endDate, keyword, offset, limit);
    }

    public List<CabinetDayStats> findTotalCabinetStatsList(Integer agentId, String beginDate, String endDate, String keyword, Integer offset, Integer limit) {
        return cabinetDayStatsMapper.findTotalCabinetStatsList(agentId, beginDate, endDate, keyword, offset, limit);
    }

    public CabinetDayStats findTotalStatsListByCabinetId(Integer agentId, String cabinetId, String beginDate, String endDate) {
        return cabinetDayStatsMapper.findTotalStatsListByCabinetId(agentId, cabinetId, beginDate, endDate);
    }

    public List<CabinetDayStats> findListByCabinetId(Integer agentId, String cabinetId) {
        return cabinetDayStatsMapper.findListByCabinetId(agentId, cabinetId);
    }

    public CabinetDayStats findForCabinet(String cabinetId, String statsDate) {
        return cabinetDayStatsMapper.findForCabinet(cabinetId,statsDate);
    }

    public List<CabinetDayStats> findForStats(Integer agentId, String statsDate, String keyword, int offset, int limit) {
        return cabinetDayStatsMapper.findForStats(agentId, statsDate, keyword,offset, limit);
    }

    public CabinetDayStats findTotalByStats(Integer agentId, String statsDate) {
        return cabinetDayStatsMapper.findTotalByStats(agentId, statsDate);
    }


}
