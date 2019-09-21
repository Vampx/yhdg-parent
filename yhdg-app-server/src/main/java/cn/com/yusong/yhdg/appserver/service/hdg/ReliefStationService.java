package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.ReliefStationMapper;
import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
@Service
public class ReliefStationService {
    @Autowired
    ReliefStationMapper reliefStationMapper;

    public List<ReliefStation> findList(Integer partnerId, Integer cityId, double lng, double lat, int offset, int limit) {
        return reliefStationMapper.findList(partnerId, cityId, lng, lat, offset, limit);
    }
}
