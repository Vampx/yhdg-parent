package cn.com.yusong.yhdg.agentserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PlatformDayStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformDayStatsService {
    @Autowired
    PlatformDayStatsMapper platformDayStatsMapper;

    public Page findPage(PlatformDayStats search) {
        Page page = search.buildPage();
        page.setTotalItems(platformDayStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(platformDayStatsMapper.findPageResult(search));
        return page;
    }

    public List<PlatformDayStats> findForExcel (PlatformDayStats search) {
        return platformDayStatsMapper.findPageResult(search);
    }
    public PlatformDayStats findByDate(String statsDate) {
        return platformDayStatsMapper.findByDate(statsDate);
    }
}
