package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceStation;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VipPriceStationService extends AbstractService {

    @Autowired
    VipPriceStationMapper vipPriceStationMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;

    public List<VipPriceStation> findListByPriceId(Long priceId) {
        List<VipPriceStation> list = vipPriceStationMapper.findListByPriceId(priceId);
        for (VipPriceStation vipPriceStation : list) {
            Station station = stationMapper.find(vipPriceStation.getStationId());
            vipPriceStation.setStationName(station.getStationName());
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipPriceStation entity) {
        String[] stationIdArr = entity.getIds().split(",");
        for (String stationId : stationIdArr) {
            VipPriceStation cct = new VipPriceStation();
            cct.setPriceId(entity.getPriceId());
            cct.setStationId(stationId);
            vipPriceStationMapper.insert(cct);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vipPriceStationMapper.delete(id);
        List<VipPriceStation> stationList = vipPriceStationMapper.findListByPriceId(priceId);
        vipPriceMapper.updateStationCount(priceId, stationList.size());
        return ExtResult.successResult();
    }
}
