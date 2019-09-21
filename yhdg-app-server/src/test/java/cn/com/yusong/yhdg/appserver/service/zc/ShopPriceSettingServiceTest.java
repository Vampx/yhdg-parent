package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ShopPriceSettingServiceTest  extends BaseJunit4Test {

    @Autowired
    ShopPriceSettingService shopPriceSettingService;

    @Test
    public void findByPriceSettingIdAll() throws Exception {

        Partner partner =newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Shop shop = newShop(agent.getId());
        insertShop(shop);
        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);
        PriceSetting priceSetting = newPriceSetting(agent.getId(),vehicleModel.getId());
        insertPriceSetting(priceSetting);
        RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
        insertRentPrice(rentPrice);
        ShopPriceSetting shopPriceSetting =newShopPriceSetting(agent.getId(),shop.getId(),priceSetting.getId().intValue());
        insertShopPriceSetting(shopPriceSetting);
        List<ShopPriceSetting> byPriceSettingIdAll = shopPriceSettingService.findByPriceSettingIdAll(priceSetting.getId());
        assertTrue(1 == byPriceSettingIdAll.size());
    }

}