package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.domain.hdg.StationBatteryType;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.StationBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.StationMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StationBatteryTypeService extends AbstractService {

    @Autowired
    StationBatteryTypeMapper stationBatteryTypeMapper;
    @Autowired
    StationMapper stationMapper;

    public List<StationBatteryType> findListByBatteryType(Integer batteryType, Integer agentId) {
        List<StationBatteryType> list = stationBatteryTypeMapper.findListByBatteryType(batteryType, agentId);
        for (StationBatteryType stationBatteryType : list) {
            Station station = stationMapper.find(stationBatteryType.getStationId());
            stationBatteryType.setStationName(station.getStationName());
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(StationBatteryType entity) {
        String[] stationIdArr = entity.getIds().split(",");
        for (String stationId : stationIdArr) {
            List<StationBatteryType> list = stationBatteryTypeMapper.findListByStation(stationId);
            if (list.size() > 0) {
                return ExtResult.failResult("包含已存在的站点");
            }
            StationBatteryType cct = new StationBatteryType();
            cct.setBatteryType(entity.getBatteryType());
            cct.setStationId(stationId);
            stationBatteryTypeMapper.insert(cct);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(String stationId, Integer batteryType) {
        stationBatteryTypeMapper.delete(stationId, batteryType);
        return ExtResult.successResult();
    }
}
