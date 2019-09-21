package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.PlatformMonthStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PlatformMonthStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformMonthStatsService {
    @Autowired
    PlatformMonthStatsMapper platformMonthStatsMapper;

    public Page findPage(PlatformMonthStats search) {
        Page page = search.buildPage();
        page.setTotalItems(platformMonthStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(platformMonthStatsMapper.findPageResult(search));
        return page;
    }

    public List<PlatformMonthStats> findForExcel (PlatformMonthStats search) {
        return platformMonthStatsMapper.findPageResult(search);
    }
}
