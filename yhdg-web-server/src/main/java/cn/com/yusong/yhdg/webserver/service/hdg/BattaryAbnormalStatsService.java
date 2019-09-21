package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
