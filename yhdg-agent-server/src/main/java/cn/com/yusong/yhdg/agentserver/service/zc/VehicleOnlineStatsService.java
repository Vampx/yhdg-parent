package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.agentserver.persistence.zc.VehicleOnlineStatsMapper;
import cn.com.yusong.yhdg.common.domain.zc.VehicleOnlineStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleOnlineStatsService {
    @Autowired
    VehicleOnlineStatsMapper vehicleOnlineStatsMapper;

    public Page findPage(VehicleOnlineStats search) {
        Page page = search.buildPage();
        page.setTotalItems(vehicleOnlineStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(vehicleOnlineStatsMapper.findPageResult(search));
        return page;
    }

}
