package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetAddressCorrectionExemptReviewMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetAddressCorrectionMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrection;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
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
public class CabinetAddressCorrectionService extends AbstractService {

    static final Logger log = LogManager.getLogger(CabinetAddressCorrectionService.class);

    @Autowired
    CabinetAddressCorrectionMapper correctionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AreaCache areaCache;

    @Autowired
    CabinetAddressCorrectionExemptReviewMapper correctionExemptReviewMapper;

    public void insert (CabinetAddressCorrection entiy){
        correctionMapper.insert(entiy);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult changePositionCustomer (String cabinetId,
                                   String cabinetName,
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

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if(cabinet == null){
            return  RestResult.result(RespCode.CODE_2.getValue(),"换电柜不存在");
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

        int agentId = cabinet.getAgentId();

        CabinetAddressCorrectionExemptReview correctionReview = correctionExemptReviewMapper.find(customer.getId());

        CabinetAddressCorrection correction = new CabinetAddressCorrection();
        correction.setAgentId(agentId);
        correction.setCabinetId(cabinetId);
        correction.setCabinetName(cabinetName);
        correction.setCityId(cityId);
        correction.setCreateTime(new Date());
        correction.setCustomerFullname(customer.getFullname());
        correction.setCustomerId(customer.getId());
        correction.setCustomerMobile(customer.getMobile());
        correction.setDistrictId(districtId);
        correction.setProvinceId(provinceId);
        correction.setLat(lat);
        correction.setLng(lng);
        correction.setMemo(memo);
        correction.setStreet(street);
        if(correctionReview == null) {
            correction.setStatus(CabinetAddressCorrection.Status.AUDIT_NO.getValue());
        }else {
            correction.setStatus(CabinetAddressCorrection.Status.AUDIT_PASS.getValue());
            String geoHash = null;
            if(lng != null && lat != null  && lng != 0 && lat != 0) {
                geoHash = GeoHashUtils.getGeoHashString(lng, lat);
            }else{
                lng = null;
                lat = null;
            }
            String address = cabinet.getAddress();
            if( provinceId != null || cityId != null || districtId != null || StringUtils.isNotEmpty(street)) {
                if (provinceId != null) {
                    address = areaCache.get(provinceId).getAreaName();
                } else {
                    address = areaCache.get(cabinet.getProvinceId()).getAreaName();
                }
                if (cityId != null) {
                    address += areaCache.get(cityId).getAreaName();
                } else {
                    address += areaCache.get(cabinet.getCityId()).getAreaName();
                }

                if (districtId != null) {
                    address += areaCache.get(districtId).getAreaName();
                }else {
                    address += areaCache.get(cabinet.getDistrictId()).getAreaName();
                }
                if (StringUtils.isNotEmpty(street)) {
                    address += street;
                }else {
                    address += cabinet.getStreet();
                }
            }
            String keyword = cabinetName + address;

            if(StringUtils.equals(address,cabinet.getAddress())){
                address = null;
            }
            if(StringUtils.equals(keyword, cabinet.getKeyword())){
                keyword = null;
            }

            if(cabinetMapper.updateLocation(cabinetId, provinceId, cityId, districtId, street, lng, lat, geoHash, address, keyword, cabinetName) == 0){
                log.error("CabinetAddressCorrectionService.createResult.updateLocation:{}","更新地理位置出错");
                return RestResult.result(RespCode.CODE_1.getValue(),"更新换电柜地理位置出错") ;
            }
        }
        correctionMapper.insert(correction);

        return  RestResult.result(RespCode.CODE_0.getValue(),"换电柜纠错位置成功");


    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult changePositionUser(String cabinetId,
                                   String cabinetName,
                                   Integer baiduCityId,
                                   String districtName,
                                   String street,
                                   Double lng,
                                   Double lat,
                                   String memo,
                                   Long userId){
        User user = userMapper.find(userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if(cabinet == null){
            return  RestResult.result(RespCode.CODE_2.getValue(),"换电柜不存在");
        }

        Area cityArea = areaCache.getByBaiduId(baiduCityId);
        Integer provinceId = null, cityId = null,districtId = null;
        if (cityArea != null) {
            provinceId = cityArea.getParentId();
            cityId = cityArea.getId();

        }
        if(areaCache.getByName(districtName) != null) {
            districtId = areaCache.getByName(districtName).getId();
        }
        int agentId = cabinet.getAgentId();

        CabinetAddressCorrection correction = new CabinetAddressCorrection();
        correction.setAgentId(agentId);
        correction.setCabinetId(cabinetId);
        correction.setCabinetName(cabinetName);
        correction.setCityId(cityId);
        correction.setCreateTime(new Date());
        correction.setDistrictId(districtId);
        correction.setProvinceId(provinceId);
        correction.setLat(lat);
        correction.setLng(lng);
        correction.setMemo(memo);
        correction.setStreet(street);
        correction.setStatus(CabinetAddressCorrection.Status.AUDIT_PASS.getValue());
        correctionMapper.insert(correction);

        String geoHash = null;
        if(lng != null && lat != null && lng != 0 && lat != 0 ) {
            geoHash = GeoHashUtils.getGeoHashString(lng, lat);
        }else{
            lng = null;
            lat = null;
        }
        String address = cabinet.getAddress();
        if( provinceId != null || cityId != null || districtId != null || StringUtils.isNotEmpty(street)) {
            if (provinceId != null) {
                address = areaCache.get(provinceId).getAreaName();
            } else {
                address = areaCache.get(cabinet.getProvinceId()).getAreaName();
            }
            if (cityId != null) {
                address += areaCache.get(cityId).getAreaName();
            } else {
                address += areaCache.get(cabinet.getCityId()).getAreaName();
            }

            if (districtId != null) {
                address += areaCache.get(districtId).getAreaName();
            }else {
                address += areaCache.get(cabinet.getDistrictId()).getAreaName();
            }
            if (StringUtils.isNotEmpty(street)) {
                address += street;
            }else {
                address += cabinet.getStreet();
            }
        }
        String cabinetName1 = cabinet.getCabinetName();
        if(StringUtils.isNotEmpty(cabinetName)) {
            cabinetName1 = cabinetName;
        }
        String keyword = cabinetName1 + address;

        if(StringUtils.equals(address,cabinet.getAddress())){
            address = null;
        }
        if(StringUtils.equals(keyword, cabinet.getKeyword())){
            keyword = null;
        }
        if(StringUtils.equals(cabinetName,cabinetName1)){
            cabinetName = null;
        }



        if(cabinetMapper.updateLocation(cabinetId, provinceId, cityId, districtId, street, lng, lat, geoHash, address, keyword, cabinetName) == 0){
            log.error("CabinetAddressCorrectionService.createResult.updateLocation:{}","更新地理位置出错");
            return RestResult.result(RespCode.CODE_1.getValue(),"更新换电柜地理位置出错") ;
        }
        return  RestResult.result(RespCode.CODE_0.getValue(),"换电柜纠错位置成功");


    }

}
