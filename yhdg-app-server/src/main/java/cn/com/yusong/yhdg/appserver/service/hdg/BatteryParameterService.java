package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryParameterMapper;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryParameterService {
    @Autowired
    BatteryParameterMapper batteryParameterMapper;

    public BatteryParameter find(String id) {
        return batteryParameterMapper.find(id);
    }
}
