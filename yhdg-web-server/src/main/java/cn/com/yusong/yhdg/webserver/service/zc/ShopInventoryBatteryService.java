package cn.com.yusong.yhdg.webserver.service.zc;


import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.ShopInventoryBatteryMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopInventoryBatteryService extends AbstractService{
    @Autowired
    ShopInventoryBatteryMapper shopInventoryBatteryMapper;

    @Autowired
    BatteryMapper batteryMapper;

    public Page findPage(ShopStoreBattery search) {
        Page page = search.buildPage();
        page.setTotalItems(shopInventoryBatteryMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopStoreBattery> shopStoreBatteries = shopInventoryBatteryMapper.findPageResult(search);
        page.setResult(shopStoreBatteries);
        return page;
    }
    public ShopStoreBattery find(long id){
        return shopInventoryBatteryMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int UnbindBattery(long id) {
        ShopStoreBattery shopStoreBattery = shopInventoryBatteryMapper.find(id);
        int effect = shopInventoryBatteryMapper.delete(id);
        Battery battery = batteryMapper.find(shopStoreBattery.getBatteryId());
        if(battery != null){
            if(battery.getCategory() == Battery.Category.RENT.getValue()){
                batteryMapper.updateShopId(battery.getId(), null, null);
            }
        }
        return effect;
    }

    public List<ShopStoreBattery> findByShop(String shopId) {
        return shopInventoryBatteryMapper.findByShopId(shopId);
    }
}
