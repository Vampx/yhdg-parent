package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StationService extends AbstractService {

    @Autowired
    StationMapper stationMapper;
    @Autowired
    MemCachedClient memCachedClient;

    public List<Station> findVipStationList(Integer agentId, String keyword, int offset, int limit) {
        return stationMapper.findVipStationList(agentId, keyword, offset, limit);
    }

    public List<Station> findBatteryStationList(Integer agentId, String keyword, int offset, int limit) {
        return stationMapper.findBatteryStationList(agentId, keyword, offset, limit);
    }

}