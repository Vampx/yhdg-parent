package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetTotalStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CabinetTotalStatsService extends AbstractService {
    @Autowired
    CabinetTotalStatsMapper cabinetTotalStatsMapper;

    public CabinetTotalStats find(String cabinetId, Integer agentId) {
        return cabinetTotalStatsMapper.find(cabinetId, agentId);
    }

    public CabinetTotalStats sumAll(Integer agentId) {
        return cabinetTotalStatsMapper.sumAll(agentId);
    }

    public List<CabinetTotalStats> findListByAgentId(Integer agentId, String keyword, int offset, int limit) {
        return cabinetTotalStatsMapper.findListByAgentId(agentId, keyword, offset, limit);
    }

    public CabinetTotalStats findCountByAgentId (Integer agentId) {
        return cabinetTotalStatsMapper.findCountByAgentId(agentId);
    }
}
