package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShopServiceTest extends BaseJunit4Test{
    @Autowired
    ShopService shopService;

    @Test
    public void findShopIdDistance() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Shop shop = newShop(agent.getId());
        shop.setLng(120.021724);
        shop.setLat(30.35371);
        shop.setGeoHash(GeoHashUtils.getGeoHashString(shop.getLng(), shop.getLat()));
        shop.setActiveStatus(Shop.ActiveStatus.ENABLE.getValue());
        shop.setKeyword("zzz");
        insertShop(shop);
        String geoHash = GeoHashUtils.getGeoHashString(shop.getLng(), shop.getLat());
        Shop shopIdDistance = shopService.findShopIdDistance(shop.getId(), geoHash.substring(0, 5), 120.021724, 30.35371);
        assertNotNull(shopIdDistance);
        double distance = shopIdDistance.getDistance();
        assertEquals(0.0,distance);
    }

}