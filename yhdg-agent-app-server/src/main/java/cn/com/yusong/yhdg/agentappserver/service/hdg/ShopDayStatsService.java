package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopDayStatsMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopDayStatsService extends AbstractService {
    @Autowired
    ShopDayStatsMapper shopDayStatsMapper;

    public ShopDayStats find(Integer agentId, String shopId, String statsDate, Integer category) {
        return shopDayStatsMapper.find(agentId, shopId, statsDate, category);
    }

    public List<ShopDayStats> findTotalShopStatsList(Integer agentId, Integer category, String beginDate, String endDate, String keyword, Integer offset, Integer limit) {
        return shopDayStatsMapper.findTotalShopStatsList(agentId, category, beginDate, endDate, keyword, offset, limit);
    }

    public ShopDayStats findTotalStatsListShopId(Integer agentId, String shopId, Integer category, String beginDate, String endDate) {
        return shopDayStatsMapper.findTotalStatsListShopId(agentId, shopId, category, beginDate, endDate);
    }

    public List<ShopDayStats> findDateRange(Integer agentId, String shopId, String beginDate, String endDate, Integer category) {
        return shopDayStatsMapper.findDateRange(agentId, shopId, beginDate, endDate, category);
    }

    public ShopDayStats statsOrderAndRefundMoney(String shopId) {
        return shopDayStatsMapper.statsOrderAndRefundMoney(shopId);
    }
}
