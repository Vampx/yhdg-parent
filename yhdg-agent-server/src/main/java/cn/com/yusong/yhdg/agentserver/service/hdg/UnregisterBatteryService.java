package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBattery;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.UnregisterBatteryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnregisterBatteryService {
    @Autowired
    UnregisterBatteryMapper unregisterBatteryMapper;
    @Autowired
    BatteryMapper batteryMapper;

    public Page findPage(UnregisterBattery search) {
        Page page = search.buildPage();
        page.setTotalItems(unregisterBatteryMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<UnregisterBattery> list = unregisterBatteryMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    public UnregisterBattery find(String id) {
        return unregisterBatteryMapper.find(id);
    }
}
