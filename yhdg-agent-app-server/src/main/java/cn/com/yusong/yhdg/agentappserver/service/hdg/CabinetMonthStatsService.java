package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetMonthStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CabinetMonthStatsService extends AbstractService {
    @Autowired
    CabinetMonthStatsMapper cabinetMonthStatsMapper;

    public List<CabinetMonthStats> findByCabinetList(Integer agentId, String cabinetId) {
        return cabinetMonthStatsMapper.findByCabinetList(agentId, cabinetId);
    }

    public CabinetMonthStats findTotal(String cabinetId) {
        return cabinetMonthStatsMapper.findTotal(cabinetId);
    }

}
