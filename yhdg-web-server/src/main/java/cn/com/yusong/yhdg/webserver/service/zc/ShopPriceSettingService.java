package cn.com.yusong.yhdg.webserver.service.zc;


import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.PriceSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.ShopPriceSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.ShopStoreVehicleMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopPriceSettingService extends AbstractService {
    @Autowired
    ShopPriceSettingMapper shopPriceSettingMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;

    @Autowired
    ShopStoreVehicleMapper shopStoreVehicleMapper;

    @Autowired
    BatteryMapper batteryMapper;

    public Page findPage(ShopPriceSetting search) {
        Page page = search.buildPage();
        page.setTotalItems(shopPriceSettingMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopPriceSetting> list = shopPriceSettingMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    public Page findByShopPage(ShopPriceSetting search) {
        Page page = search.buildPage();
        page.setTotalItems(shopPriceSettingMapper.findByShopPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopPriceSetting> list = shopPriceSettingMapper.findByShopPageResult(search);
        page.setResult(list);
        return page;
    }

    public ShopPriceSetting find(Integer id) {
        return shopPriceSettingMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(ShopPriceSetting shopPriceSetting) {
        if (shopPriceSettingMapper.fondByShopCount(shopPriceSetting.getShopId(), shopPriceSetting.getPriceSettingId()) > 0) {
            return ExtResult.failResult("存在重复套餐");
        }
        if (priceSettingMapper.find(shopPriceSetting.getPriceSettingId()) == null) {
            return ExtResult.failResult("套餐不存在");
        }
        PriceSetting priceSetting = priceSettingMapper.find(shopPriceSetting.getPriceSettingId());
        Shop shop = shopMapper.find(shopPriceSetting.getShopId());
        ShopPriceSetting search = new ShopPriceSetting();
        search.setShopId(shop.getId());
        search.setShopName(shop.getShopName());
        search.setAgentId(priceSetting.getAgentId());
        search.setAgentName(priceSetting.getAgentName());
        search.setPriceSettingId(shopPriceSetting.getPriceSettingId());
        search.setCreateTime(new Date());
        List<ShopStoreVehicle> byPriceSettingList = shopStoreVehicleMapper.findByPriceSettingList(shopPriceSetting.getShopId(), shopPriceSetting.getPriceSettingId());
        if (byPriceSettingList.size() == 0) {
            search.setVehicleCount(0);
        } else {
            search.setVehicleCount(byPriceSettingList.size());
        }
        shopPriceSettingMapper.insert(search);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public int delete(Integer id) {
        return shopPriceSettingMapper.delete(id);
    }
}
