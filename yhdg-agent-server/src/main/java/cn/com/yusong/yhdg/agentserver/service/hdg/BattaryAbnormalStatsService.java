package cn.com.yusong.yhdg.agentserver.service.hdg;


import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BattaryAbnormalStatsService extends AbstractService {

    static Logger log = LoggerFactory.getLogger(BattaryAbnormalStatsService.class);

    @Autowired
    BatteryMapper batteryMapper;

    public List<Battery> findList(Battery search) {
        List<Battery> battery = batteryMapper.findByAbnormalAll(search);
        return battery;
    }


}
