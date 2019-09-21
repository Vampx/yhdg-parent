package cn.com.yusong.yhdg.agentserver.service.hdg;


import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopStoreBatteryService extends AbstractService {
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    BatteryMapper batteryMapper;
    public Page findPage(ShopStoreBattery search) {
        Page page = search.buildPage();
        page.setTotalItems(shopStoreBatteryMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopStoreBattery> shopStoreBatteries = shopStoreBatteryMapper.findPageResult(search);
        page.setResult(shopStoreBatteries);
        return page;
    }

    public ShopStoreBattery find(long id){
        return shopStoreBatteryMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int UnbindBattery(long id) {
        ShopStoreBattery shopStoreBattery = shopStoreBatteryMapper.find(id);
        int effect = shopStoreBatteryMapper.delete(id);
        Battery battery = batteryMapper.find(shopStoreBattery.getBatteryId());
        if(battery != null){
            if(battery.getCategory() == Battery.Category.RENT.getValue()){
                batteryMapper.updateShopId(battery.getId(), null, null);
            }
        }
        return effect;
    }

    public ShopStoreBattery findByBattery(String batteryId){
        return shopStoreBatteryMapper.findByBattery(batteryId);
    }
}
