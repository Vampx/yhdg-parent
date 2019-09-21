package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.service.AbstractService;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetAddressCorrectionMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CabinetAddressCorrectionService extends AbstractService {

    @Autowired
    CabinetAddressCorrectionMapper cabinetAddressCorrectionMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetMapper cabinetMapper;

    public Page findPage(CabinetAddressCorrection search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetAddressCorrectionMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetAddressCorrection> dispatcherAreaList = cabinetAddressCorrectionMapper.findPageResult(search);
        page.setResult(setAreaProperties(areaCache, dispatcherAreaList));
        return page;
    }

    public CabinetAddressCorrection find(long id) {
        CabinetAddressCorrection cabinetAddressCorrection = cabinetAddressCorrectionMapper.find(id);
        return (CabinetAddressCorrection) setAreaProperties(areaCache, cabinetAddressCorrection);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateStatus(CabinetAddressCorrection entity) {
        if (entity.getStatus() == CabinetAddressCorrection.Status.AUDIT_PASS.getValue()) {
            Cabinet cabinet = cabinetMapper.find(entity.getCabinetId());
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
            String address = cabinet.getAddress();
            if( provinceId != null || cityId != null || districtId != null || StringUtils.isNotEmpty(street)) {
                if (provinceId != null) {
                    if (areaCache.get(provinceId) != null) {
                        address = areaCache.get(provinceId).getAreaName();
                    }
                } else {
                    address = areaCache.get(cabinet.getProvinceId()).getAreaName();
                }
                if (cityId != null) {
                    if (areaCache.get(cityId) != null) {
                        address += areaCache.get(cityId).getAreaName();
                    }
                } else {
                    address += areaCache.get(cabinet.getCityId()).getAreaName();
                }

                if (districtId != null) {
                    if (areaCache.get(districtId) != null) {
                        address += areaCache.get(districtId).getAreaName();
                    }
                }else {
                    address += areaCache.get(cabinet.getDistrictId()).getAreaName();
                }
                if (StringUtils.isNotEmpty(street)) {
                    address += street;
                }else {
                    address += cabinet.getStreet();
                }
            }

            String cabinetName = null;
            if(StringUtils.isNotEmpty(entity.getCabinetName())) {
                cabinetName = entity.getCabinetName();
            } else {
                cabinetName = cabinet.getCabinetName();
            }
            String keyword = cabinetName + address;

            if(StringUtils.equals(address,cabinet.getAddress())){
                address = null;
            }
            if(StringUtils.equals(keyword, cabinet.getKeyword())){
                keyword = null;
            }

            if(cabinetMapper.updateLocation(entity.getCabinetId(), provinceId, cityId, districtId, street, lng, lat, geoHash, address, keyword, cabinetName) == 0){
                throw new RuntimeException();
            }
            cabinetAddressCorrectionMapper.updateStatus(entity.getId(), entity.getStatus());
        } else {
            cabinetAddressCorrectionMapper.updateStatus(entity.getId(), entity.getStatus());
        }

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(long id) {
        int total = cabinetAddressCorrectionMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("删除失败");
        }
        return ExtResult.successResult();
    }
}
