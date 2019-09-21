package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BackBatteryOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.common.domain.hdg.BackBatteryOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackBatteryOrderService extends AbstractService {
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;

    public Page findPage(BackBatteryOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(backBatteryOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BackBatteryOrder> batteryList = backBatteryOrderMapper.findPageResult(search);
        for (BackBatteryOrder backBatteryOrder: batteryList) {
            if (StringUtils.isNotEmpty(backBatteryOrder.getBatteryId())) {
                Battery battery = batteryMapper.find(backBatteryOrder.getBatteryId());
                if (battery != null) {
                    if (battery.getType() != null) {
                        String type = findBatteryType(battery.getType()).getTypeName();
                        backBatteryOrder.setBatteryType(type);
                    }
                }
            }
        }
        page.setResult(batteryList);
        return page;
    }


    public BackBatteryOrder find(String id) {
        return backBatteryOrderMapper.find(id);
    }
}
