package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetIncomeTemplateMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetInstallRecordMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetIncomeTemplate;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CabinetInstallRecordService extends AbstractService {

    @Autowired
    CabinetInstallRecordMapper cabinetInstallRecordMapper;
    @Autowired
    CabinetIncomeTemplateMapper cabinetIncomeTemplateMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AreaCache areaCache;

    public ExtResult insert(CabinetInstallRecord entity) {
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult zhizuUPLine(Cabinet cabinet, String salesman, String address, Integer areaId, String street, Double lng, Double lat) {
        Date now = new Date();

        if(areaId != null){
            Area area = areaCache.get(areaId);
            Integer provinceId = null, cityId = null,districtId = null;
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

            cabinet.setProvinceId(provinceId);
            cabinet.setCityId(cityId);
            cabinet.setDistrictId(districtId);
        }

        String geoHash = null;
        if(lng != null && lat != null && lng != 0 && lat != 0 ) {
            geoHash = GeoHashUtils.getGeoHashString(lng, lat);
        }

        cabinet.setLng(lng);
        cabinet.setLat(lat);
        cabinet.setGeoHash(geoHash);
        cabinet.setStreet(street);
        cabinet.setAddress(address);
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setUpLineTime(now);

        int result =  cabinetMapper.updateUpLineStatus(cabinet);
       if(result > 0 ){
           CabinetInstallRecord entity = new CabinetInstallRecord();
           entity.setAgentId(cabinet.getAgentId());
           entity.setAgentName(cabinet.getAgentName());
           entity.setCabinetId(cabinet.getId());
           entity.setPermitExchangeVolume(cabinet.getPermitExchangeVolume());
           entity.setChargeFullVolume(cabinet.getChargeFullVolume());
           entity.setForegiftMoney(0);
           entity.setRentMoney(0);
           entity.setRentPeriodType(0);
           entity.setStatus(2);
           entity.setSalesman(salesman);
           entity.setCreateTime(now);
           cabinetInstallRecordMapper.insert(entity);
           return RestResult.result(RespCode.CODE_0.getValue(),null);

       }
        return RestResult.result(RespCode.CODE_2.getValue(),"上线失败");
    }
}
