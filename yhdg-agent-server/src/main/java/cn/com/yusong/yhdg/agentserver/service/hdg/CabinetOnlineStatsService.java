package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOnlineStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetOnlineStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetOnlineStatsService {
    @Autowired
    CabinetOnlineStatsMapper cabinetOnlineStatsMapper;

    public Page findPage(CabinetOnlineStats search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetOnlineStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(cabinetOnlineStatsMapper.findPageResult(search));
        return page;
    }

}
