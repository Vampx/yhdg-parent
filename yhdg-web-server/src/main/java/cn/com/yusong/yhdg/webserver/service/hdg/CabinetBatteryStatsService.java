package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetBatteryStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetBatteryStatsService extends AbstractService {

    @Autowired
    CabinetBatteryStatsMapper cabinetBatteryStatsMapper;

    public Page findPage(CabinetBatteryStats search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetBatteryStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetBatteryStats> list = cabinetBatteryStatsMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    public CabinetBatteryStats find (long id) {
        return cabinetBatteryStatsMapper.find(id);
    }

}
