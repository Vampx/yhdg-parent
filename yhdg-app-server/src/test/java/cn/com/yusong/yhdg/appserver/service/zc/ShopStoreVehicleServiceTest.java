package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ShopStoreVehicleServiceTest extends BaseJunit4Test{
    @Autowired
    ShopStoreVehicleService shopStoreVehicleService;

    @Test
    public void findByVehicleId() throws Exception {
    }

    @Test
    public void findByVehicleCount() throws Exception {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        shop.setLng(120.021724);
        shop.setLat(30.35371);
        shop.setGeoHash(GeoHashUtils.getGeoHashString(shop.getLng(), shop.getLat()));
        shop.setActiveStatus(Shop.ActiveStatus.ENABLE.getValue());
        shop.setKeyword("zzz");
        insertShop(shop);

        VehicleModel vehicleModel =newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        PriceSetting priceSetting =newPriceSetting(agent.getId(),vehicleModel.getId());
        insertPriceSetting(priceSetting);
        Vehicle vehicle =newVehicle(agent.getId(),vehicleModel.getId());
        insertVehicle(vehicle);

        ShopStoreVehicle shopStoreVehicle =newShopStoreVehicle(agent.getId(),shop.getId(),priceSetting.getId(),vehicle.getId());
        insertShopStoreVehicle(shopStoreVehicle);

        int byVehicleCount = shopStoreVehicleService.findByVehicleCount(shop.getId(), shopStoreVehicle.getPriceSettingId().intValue());
        assertEquals(1,byVehicleCount);

    }

}