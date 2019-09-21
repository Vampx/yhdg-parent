package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShopService extends AbstractService {
	static Logger log = LogManager.getLogger(ShopService.class);

	@Autowired
	AreaCache areaCache;
	@Autowired
	ShopMapper shopMapper;

	public Shop find(String id) {
		return shopMapper.find(id);
	}

	public List<Shop> findNearestInfo(String geoHash, double lng, double lat, Integer cityId, String keyword, int offset, int limit) {
		return (List<Shop>) setAreaProperties(areaCache,
				shopMapper.findNearestInfo(geoHash, keyword, lng, lat, cityId, offset, limit));
	}

	public List<Shop> findNearest(String geoHash, double lng, double lat, Integer cityId, String keyword, int offset, int limit) {
		return (List<Shop>) setAreaProperties(areaCache,
				shopMapper.findNearest(geoHash, keyword, lng, lat, cityId, offset, limit));
	}
	public Shop findShopIdDistance(String ShopId,String geoHash, double lng, double lat) {
		return  shopMapper.findShopIdDistance(ShopId, geoHash,  lng, lat);
	}

	public RestResult findAddressList() {
		List<Shop> addressList = shopMapper.findAddressList();
		Set<Map> maps = new LinkedHashSet<Map>();

		for (Shop shop : addressList) {
			Map province = new HashMap();
			Area area = findArea(shop.getProvinceId());
			province.put("provinceId", area.getId());
			province.put("provinceName", area.getAreaName());
			maps.add(province);
		}
		for (Map map : maps) {
			List<Map> cityList = new ArrayList<Map>();
			for (Shop shop : addressList) {
				if (map.get("provinceId").equals(shop.getProvinceId())) {
					Map city = new HashMap();
					Area area = findArea(shop.getCityId());
					city.put("areaName", area.getAreaName());
					city.put("areaId", area.getId());
					city.put("lng", area.getLongitude());
					city.put("lat", area.getLatitude());
					cityList.add(city);
				}
			}
			map.remove("provinceId");
			map.put("cityList", cityList);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, maps);
	}


	public RestResult findAddVehicleShopList() {
		List<Shop> addressList = shopMapper.findAddVehicleShopList();
		Set<Map> maps = new LinkedHashSet<Map>();

		for (Shop shop : addressList) {
			Map province = new HashMap();
			Area area = findArea(shop.getProvinceId());
			province.put("provinceId", area.getId());
			province.put("provinceName", area.getAreaName());
			maps.add(province);
		}
		for (Map map : maps) {
			List<Map> cityList = new ArrayList<Map>();
			for (Shop shop : addressList) {
				if (map.get("provinceId").equals(shop.getProvinceId())) {
					Map city = new HashMap();
					Area area = findArea(shop.getCityId());
					city.put("areaName", area.getAreaName());
					city.put("areaId", area.getId());
					city.put("lng", area.getLongitude());
					city.put("lat", area.getLatitude());
					cityList.add(city);
				}
			}
			map.remove("provinceId");
			map.put("cityList", cityList);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, maps);
	}

}
