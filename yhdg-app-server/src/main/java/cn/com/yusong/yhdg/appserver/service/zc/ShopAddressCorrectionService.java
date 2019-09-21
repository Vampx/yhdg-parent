package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetAddressCorrectionExemptReviewMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetAddressCorrectionMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.ShopAddressCorrectionExemptReviewMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.ShopAddressCorrectionMapper;
import cn.com.yusong.yhdg.appserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.service.AbstractService;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
@Service
public class ShopAddressCorrectionService extends AbstractService {

    static final Logger log = LogManager.getLogger(ShopAddressCorrectionService.class);

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    ShopAddressCorrectionExemptReviewMapper shopAddressCorrectionExemptReviewMapper;
    @Autowired
    ShopAddressCorrectionMapper shopAddressCorrectionMapper;

    @Transactional(rollbackFor = Throwable.class)
    public RestResult changePositionCustomer (String shopId,
                                   String shopName,
                                   Integer areaId,
                                   String street,
                                   Double lng,
                                   Double lat,
                                   String memo,
                                   Long customerId){
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        Shop shop = shopMapper.find(shopId);
        if(shop == null){
            return  RestResult.result(RespCode.CODE_2.getValue(),"门店不存在");
        }

        Area area = areaCache.get(areaId);
        Integer provinceId = null, cityId = null, districtId = null;
        if(area == null) {
            return  RestResult.result(RespCode.CODE_2.getValue(),"区域不存在");
        }

        if (area.getAreaLevel() == 3) {
            districtId = area.getId();
            cityId = area.getParentId();
            provinceId = areaCache.get(cityId).getParentId();
        } else if (area.getAreaLevel() == 2) {
            cityId = area.getId();
            provinceId = areaCache.get(cityId).getParentId();
        } else if (area.getAreaLevel() == 1) {
            provinceId = area.getId();
        }

        int agentId = shop.getAgentId();

        ShopAddressCorrectionExemptReview shopAddressCorrectionExemptReview = shopAddressCorrectionExemptReviewMapper.find(customer.getId());
        ShopAddressCorrection shopAddressCorrection = new ShopAddressCorrection();
        shopAddressCorrection.setAgentId(agentId);
        shopAddressCorrection.setShopId(shopId);
        shopAddressCorrection.setShopName(shopName);
        shopAddressCorrection.setCityId(cityId);
        shopAddressCorrection.setCreateTime(new Date());
        shopAddressCorrection.setCustomerFullname(customer.getFullname());
        shopAddressCorrection.setCustomerId(customer.getId());
        shopAddressCorrection.setCustomerMobile(customer.getMobile());
        shopAddressCorrection.setDistrictId(districtId);
        shopAddressCorrection.setProvinceId(provinceId);
        shopAddressCorrection.setLat(lat);
        shopAddressCorrection.setLng(lng);
        shopAddressCorrection.setMemo(memo);
        shopAddressCorrection.setStreet(street);
        if(shopAddressCorrectionExemptReview == null) {
            shopAddressCorrection.setStatus(CabinetAddressCorrection.Status.AUDIT_NO.getValue());
        }else {
            shopAddressCorrection.setStatus(CabinetAddressCorrection.Status.AUDIT_PASS.getValue());
            String geoHash = null;
            if(lng != null && lat != null  && lng != 0 && lat != 0) {
                geoHash = GeoHashUtils.getGeoHashString(lng, lat);
            }else{
                lng = null;
                lat = null;
            }
            String address = shop.getAddress();
            if( provinceId != null || cityId != null || districtId != null || StringUtils.isNotEmpty(street)) {
                if (provinceId != null) {
                    address = areaCache.get(provinceId).getAreaName();
                } else {
                    address = areaCache.get(shop.getProvinceId()).getAreaName();
                }
                if (cityId != null) {
                    address += areaCache.get(cityId).getAreaName();
                } else {
                    address += areaCache.get(shop.getCityId()).getAreaName();
                }

                if (districtId != null) {
                    address += areaCache.get(districtId).getAreaName();
                }else {
                    address += areaCache.get(shop.getDistrictId()).getAreaName();
                }
                if (StringUtils.isNotEmpty(street)) {
                    address += street;
                }else {
                    address += shop.getStreet();
                }
            }
            String keyword = shopName + address;

            if(StringUtils.equals(address,shop.getAddress())){
                address = null;
            }
            if(StringUtils.equals(keyword, shop.getKeyword())){
                keyword = null;
            }

            if(shopMapper.updateLocation(shopId, provinceId, cityId, districtId, street, lng, lat, geoHash, address, keyword, shopName) == 0){
                log.error("CabinetAddressCorrectionService.createResult.updateLocation:{}","更新地理位置出错");
                return RestResult.result(RespCode.CODE_1.getValue(),"更新换电柜地理位置出错") ;
            }
        }
        shopAddressCorrectionMapper.insert(shopAddressCorrection);

        return  RestResult.result(RespCode.CODE_0.getValue(),"换电柜纠错位置成功");
    }


}
