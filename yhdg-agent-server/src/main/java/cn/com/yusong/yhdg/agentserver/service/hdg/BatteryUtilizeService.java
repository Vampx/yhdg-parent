package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryUtilize;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryUtilizeMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryUtilizeService extends AbstractService {

    @Autowired
    BatteryUtilizeMapper batteryUtilizeMapper;

    public Page findPage(BatteryUtilize search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryUtilizeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batteryUtilizeMapper.findPageResult(search));
        return page;
    }
}
