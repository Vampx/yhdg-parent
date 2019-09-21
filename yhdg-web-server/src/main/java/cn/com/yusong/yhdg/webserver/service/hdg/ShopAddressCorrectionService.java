package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrection;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.service.AbstractService;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopAddressCorrectionMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopAddressCorrectionService extends AbstractService {

    @Autowired
    ShopAddressCorrectionMapper shopAddressCorrectionMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    ShopMapper shopMapper;

    public Page findPage(ShopAddressCorrection search) {
        Page page = search.buildPage();
        page.setTotalItems(shopAddressCorrectionMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopAddressCorrection> dispatcherAreaList = shopAddressCorrectionMapper.findPageResult(search);
        page.setResult(setAreaProperties(areaCache, dispatcherAreaList));
        return page;
    }

    public ShopAddressCorrection find(long id) {
        ShopAddressCorrection shopAddressCorrection = shopAddressCorrectionMapper.find(id);
        return (ShopAddressCorrection) setAreaProperties(areaCache, shopAddressCorrection);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateStatus(ShopAddressCorrection entity) {
        if (entity.getStatus() == ShopAddressCorrection.Status.AUDIT_PASS.getValue()) {
            Shop shop = shopMapper.find(entity.getShopId());
            Double lng = entity.getLng();
            Double lat = entity.getLat();
            Integer provinceId = entity.getProvinceId() ;
            Integer cityId = entity.getCityId() ;
            Integer districtId = entity.getDistrictId() ;
            String street = entity.getStreet();
            String geoHash = null;
            if(entity.getLng() != null && entity.getLat() != null  && entity.getLng() != 0 && entity.getLat() != 0) {
                geoHash = GeoHashUtils.getGeoHashString(entity.getLng(), entity.getLat());
            }
            String address = shop.getAddress();
            if( provinceId != null || cityId != null || districtId != null || StringUtils.isNotEmpty(street)) {
                if (provinceId != null) {
                    if (areaCache.get(provinceId) != null) {
                        address = areaCache.get(provinceId).getAreaName();
                    }
                } else {
                    address = areaCache.get(shop.getProvinceId()).getAreaName();
                }
                if (cityId != null) {
                    if (areaCache.get(cityId) != null) {
                        address += areaCache.get(cityId).getAreaName();
                    }
                } else {
                    address += areaCache.get(shop.getCityId()).getAreaName();
                }

                if (districtId != null) {
                    if (areaCache.get(districtId) != null) {
                        address += areaCache.get(districtId).getAreaName();
                    }
                }else {
                    address += areaCache.get(shop.getDistrictId()).getAreaName();
                }
                if (StringUtils.isNotEmpty(street)) {
                    address += street;
                }else {
                    address += shop.getStreet();
                }
            }

            String shopName = null;
            if(StringUtils.isNotEmpty(entity.getShopName())) {
                shopName = entity.getShopName();
            } else {
                shopName = shop.getShopName();
            }
            String keyword = shopName + address;

            if(StringUtils.equals(address,shop.getAddress())){
                address = null;
            }

            if(shopMapper.updateLocation(entity.getShopId(), shopName, provinceId, cityId, districtId, street, lng, lat, geoHash, address) == 0){
                throw new RuntimeException();
            }
            shopAddressCorrectionMapper.updateStatus(entity.getId(), entity.getStatus());
        } else {
            shopAddressCorrectionMapper.updateStatus(entity.getId(), entity.getStatus());
        }

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(long id) {
        int total = shopAddressCorrectionMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("删除失败");
        }
        return ExtResult.successResult();
    }
}
