package cn.com.yusong.yhdg.staticserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.service.AbstractService;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.BatteryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryService extends AbstractService {
    @Autowired
    BatteryMapper batteryMapper;

    public Battery find(String id) {
        return batteryMapper.find(id);
    }

}
