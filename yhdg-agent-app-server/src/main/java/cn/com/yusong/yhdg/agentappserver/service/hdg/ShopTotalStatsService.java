package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopTotalStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopTotalStatsService extends AbstractService {
    @Autowired
    ShopTotalStatsMapper shopTotalStatsMapper;

    public ShopTotalStats find(String shopId, Integer category, Integer agentId) {
        return shopTotalStatsMapper.find(shopId, category, agentId);
    }

    public List<ShopTotalStats> findListByAgentId(Integer agentId, String keyword, int category, int offset, int limit) {
        return shopTotalStatsMapper.findListByAgentId(agentId, keyword, category, offset, limit);
    }

    public ShopTotalStats sumAll(Integer agentId) {
        return shopTotalStatsMapper.sumAll(agentId);
    }

    public ShopTotalStats findCountByAgentId (Integer agentId, Integer category) {
        return shopTotalStatsMapper.findCountByAgentId(agentId,category);
    }
}
