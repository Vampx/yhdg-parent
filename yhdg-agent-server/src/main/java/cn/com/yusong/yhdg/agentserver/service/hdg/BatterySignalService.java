package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatterySignal;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatterySignalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatterySignalService {

    @Autowired
    BatterySignalMapper batterySignalMapper;

    public Page findPage(BatterySignal search) {
        Page page = search.buildPage();
        page.setTotalItems(batterySignalMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatterySignal> list = batterySignalMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }
}
