package cn.com.yusong.yhdg.frontserver.service;

import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {

    public List<? extends AreaEntity> setAreaProperties(AreaCache areaCache, List<? extends AreaEntity> list) {
        for (AreaEntity site : list) {
            setAreaProperties(areaCache, site);
        }

        return list;
    }

    public AreaEntity setAreaProperties(AreaCache areaCache, AreaEntity areaEntity) {
        if (areaEntity == null) {
            return null;
        }

        if (areaEntity.getProvinceId() != null) {
            Area area = areaCache.get(areaEntity.getProvinceId());
            if (area != null) {
                areaEntity.setProvinceName(area.getAreaName());
            }
        }
        if (areaEntity.getCityId() != null) {
            Area area = areaCache.get(areaEntity.getCityId());
            if (area != null) {
                areaEntity.setCityName(area.getAreaName());
            }
        }
        if (areaEntity.getDistrictId() != null) {
            Area area = areaCache.get(areaEntity.getDistrictId());
            if (area != null) {
                areaEntity.setDistrictName(area.getAreaName());
            }
        }

        return areaEntity;
    }
}
