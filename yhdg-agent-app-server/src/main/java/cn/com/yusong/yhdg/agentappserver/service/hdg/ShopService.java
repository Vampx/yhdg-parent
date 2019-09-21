package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopService extends AbstractService {
	@Autowired
    ShopMapper shopMapper;
	@Autowired
    UserMapper userMapper;
	@Autowired
    AreaCache areaCache;
	@Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
	@Autowired
    CustomerMapper customerMapper;

	public Shop find(String id) {
		return shopMapper.find(id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public int setPayPassword(String id, String password) {
		return shopMapper.updatePayPassword(id, password);
	}

	public List<Shop> findList(Integer agentId, String keyword, Integer offset, Integer limit) {
		return (List<Shop>) setAreaProperties(areaCache, shopMapper.findList(agentId, keyword, offset, limit));
	}

	public List<Shop> findHdVipShopList(Integer agentId, String keyword, int offset, int limit) {
		return shopMapper.findHdVipShopList(agentId, keyword, offset, limit);
	}

	public int findShopCount(Integer agentId) {
		return shopMapper.findShopCount(agentId);
	}

	public List<Shop> findZdVipShopList(Integer agentId, String keyword, int offset, int limit) {
		return shopMapper.findZdVipShopList(agentId, keyword, offset, limit);
	}

	public Map findDetail(String id) {
		Shop shop = shopMapper.find(id);
		if (shop == null) {
			return null;
		}
		Map line = new HashMap();
		line.put("id", shop.getId());
		line.put("address", shop.getAddress());
		line.put("shopName", shop.getShopName());
		line.put("linkname", shop.getLinkname());
		line.put("tel", shop.getTel());
		line.put("provinceId", shop.getProvinceId());
		line.put("cityId", shop.getCityId());
		line.put("districtId", shop.getDistrictId());
		line.put("street", shop.getStreet());
		line.put("openTime", shop.getWorkTime());

		String[] imagePath = {shop.getImagePath1(), shop.getImagePath2(), shop.getImagePath3()};
		line.put("imagePath", imagePath);

		return line;
	}


	public String findMaxId() {
		String date = DateFormatUtils.format(new Date(), "yyyyMMdd");
		String id = shopMapper.findMaxId(date);
		if (StringUtils.isEmpty(id)) {
			id = String.format("%s%0" + Constant.CABINET_ID_SEQUENCE_LENGTH + "d", date, 1);
		} else {
			String num = id.substring(id.length() - Constant.CABINET_ID_SEQUENCE_LENGTH);
			if (Long.parseLong(num) >= Math.pow(10, Constant.CABINET_ID_SEQUENCE_LENGTH) - 1) {
				throw new RuntimeException("今日门店数量已达到上限");
			}
			id = String.valueOf(Long.parseLong(id) + 1);
		}
		return id;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(String shopName, Integer areaId, String street, String openTime, Long[] userList, Double lng, Double lat, String[] imagePath, Integer agentId) {
		if (imagePath.length > 3) {
			return ExtResult.failResult("图片信息错误！");
		}

		if (StringUtils.length(street) > 20) {
			return ExtResult.failResult("街道信息过长！");
		}

		for (int i = 0; i < imagePath.length; i++) {
			if (StringUtils.length(imagePath[i]) > 120) {
				return ExtResult.failResult("图片路径过长！");
			}
		}

		Shop shop = new Shop();
		shop.setId(findMaxId());
		shop.setAgentId(agentId);
		shop.setActiveStatus(Shop.ActiveStatus.ENABLE.getValue());
		shop.setPlatformRatio(0);
		shop.setAgentRatio(0);
		shop.setBalance(0);
		shop.setStreet(street);
		shop.setShopName(shopName);

		Area area = areaCache.get(areaId);
		if (area == null) {
			return ExtResult.failResult("地址信息错误！");
		}
		//根据areaId设置地址信息
		if (area.getAreaLevel() == 1) {
			shop.setProvinceId(areaId);
			String areaName = area.getAreaName();
			shop.setAddress(areaName + street);
		} else if (area.getAreaLevel() == 2) {
			shop.setProvinceId(area.getParentId());
			shop.setCityId(areaId);
			if (area.getParentId() != null) {
				Area provinceArea = areaCache.get(shop.getProvinceId());
				if (provinceArea != null) {
					shop.setAddress(provinceArea.getAreaName() + area.getAreaName() + street);
				} else {
					shop.setAddress(area.getAreaName() + street);
				}
			} else {
				shop.setAddress(area.getAreaName() + street);
			}
		} else if (area.getAreaLevel() == 3) {
			shop.setCityId(area.getParentId());
			shop.setDistrictId(areaId);
			if (area.getParentId() != null) {
				Area cityArea = areaCache.get(area.getParentId());
				if (cityArea != null) {
					if (cityArea.getParentId() != null) {
						shop.setProvinceId(cityArea.getParentId());
						Area provinceArea = areaCache.get(cityArea.getParentId());
						if (provinceArea != null) {
							shop.setAddress(provinceArea.getAreaName() + cityArea.getAreaName() + area.getAreaName() + street);
						} else {
							shop.setAddress(cityArea.getAreaName() + area.getAreaName() + street);
						}
					} else {
						shop.setAddress(cityArea.getAreaName() + area.getAreaName() + street);
					}
				} else {
					shop.setAddress(area.getAreaName() + street);
				}
			} else {
				shop.setAddress(area.getAreaName() + street);
			}
		}
		shop.setWorkTime(openTime);
		shop.setLng(lng);
		shop.setLat(lat);
		shop.setGeoHash(GeoHashUtils.getGeoHashString(shop.getLng(), shop.getLat()));
		shop.setKeyword(shop.getId() + shop.getAddress() + shop.getShopName());
		shop.setCreateTime(new Date());

		for (int i = 0; i < userList.length; i++) {
			long userId = userList[i];
			User user = userMapper.find(userId);
			if (user == null) {
				return ExtResult.failResult("用户信息错误！");
			}
		}

		for (int i = 0; i < imagePath.length; i++) {
			if (shop.getImagePath1() == null) {
				shop.setImagePath1(imagePath[i]);
				continue;
			}
			if (shop.getImagePath2() == null) {
				shop.setImagePath2(imagePath[i]);
				continue;
			}
			if (shop.getImagePath3() == null) {
				shop.setImagePath3(imagePath[i]);
				continue;
			}
		}

		//新建门店
		shopMapper.insert(shop);

		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public RestResult update(String id, String shopName, String linkname, String tel, String workTime) {
		Shop shop = new Shop();
		shop.setId(id);
		shop.setShopName(shopName);
		shop.setLinkname(linkname);
		shop.setTel(tel);
		shop.setWorkTime(workTime);
		shopMapper.updateInfo(shop);
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	@Transactional(rollbackFor = Throwable.class)
	public RestResult updatePayPeople(String id, String payPeopleMobile, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId) {
		Shop shop = new Shop();
		shop.setId(id);
		shop.setPayPeopleMobile(payPeopleMobile);
		shop.setPayPeopleName(payPeopleName);
		if (payPeopleMpOpenId != null) {
			shop.setPayPeopleMpOpenId(payPeopleMpOpenId);
		}
		if (payPeopleFwOpenId != null) {
			shop.setPayPeopleFwOpenId(payPeopleFwOpenId);
		}
		shopMapper.updateInfo(shop);
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	public ExtResult delete(String id) {
		if (shopMapper.find(id) == null) {
			return ExtResult.failResult("记录不存在!");
		}
		int result = shopMapper.delete(id);
		if (result == 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败!");
		}
	}

	public List<Shop> findByAgentId(Integer agentId, String keyword, Double lat, Double lng, Integer offset, Integer limit) {
		return shopMapper.findByAgentId(agentId, keyword, lat, lng, offset, limit);
	}
}
