package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopDayStatsMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopTotalStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopTotalStatsService extends AbstractService{
    @Autowired
    ShopTotalStatsMapper shopTotalStatsMapper;

    public Page findPage(ShopTotalStats search) {
        Page page = search.buildPage();
        page.setTotalItems(shopTotalStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopTotalStats> shopTotalStatsList = shopTotalStatsMapper.findPageResult(search);
        for (ShopTotalStats shopTotalStats : shopTotalStatsList) {
            if (shopTotalStats.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(shopTotalStats.getAgentId());
                if (agentInfo != null) {
                    shopTotalStats.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(shopTotalStatsList);
        return page;
    }

    public List<ShopTotalStats> findForExcel (ShopTotalStats search) {
        return shopTotalStatsMapper.findPageResult(search);
    }
}
