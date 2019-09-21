package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
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

public class ShopServiceTest extends BaseJunit4Test {
	@Autowired
	ShopService service;

	@Test
	public void findNearest() {
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
		List<Integer> list = new ArrayList<Integer>();

		assertFalse(service.findNearest(geoHash.substring(0, 5), 120.021724, 30.35371, null, "zzz", 0, 100).isEmpty());
		double distance = service.findNearest(geoHash.substring(0, 5), 120.021724, 30.35371, null, "zzz", 0, 100).get(0).getDistance();
		assertEquals(0.0, distance);
	}

	@Test
	public void findAddVehicleShopList(){
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Shop shop = newShop(agent.getId());
		shop.setIsVehicle(1);
		shop.setIsExchange(50);
		shop.setIsRent(20);
		insertShop(shop);
		RestResult addVehicleShopList = service.findAddVehicleShopList();
		assertTrue(0==addVehicleShopList.getCode());

	}

}
