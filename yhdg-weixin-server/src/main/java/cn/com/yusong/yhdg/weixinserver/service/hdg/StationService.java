package cn.com.yusong.yhdg.weixinserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.weixinserver.persistence.hdg.StationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationService {
    @Autowired
    StationMapper stationMapper;

    public Station find(String id) {
        return stationMapper.find(id);
    }
}
