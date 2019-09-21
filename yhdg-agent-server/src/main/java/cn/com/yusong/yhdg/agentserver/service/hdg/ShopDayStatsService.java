package cn.com.yusong.yhdg.agentserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopDayStatsMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopDayStatsService extends AbstractService{
    @Autowired
    ShopDayStatsMapper shopDayStatsMapper;

    public Page findPage(ShopDayStats search) {
        Page page = search.buildPage();
        page.setTotalItems(shopDayStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopDayStats> shopDayStatsList = shopDayStatsMapper.findPageResult(search);
        for (ShopDayStats shopDayStats : shopDayStatsList) {
            if (shopDayStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(shopDayStats.getAgentId());
                if (agentInfo != null) {
                    shopDayStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(shopDayStatsList);
        return page;
    }

    public List<ShopDayStats> findForExcel (ShopDayStats search) {
        return shopDayStatsMapper.findPageResult(search);
    }
}
