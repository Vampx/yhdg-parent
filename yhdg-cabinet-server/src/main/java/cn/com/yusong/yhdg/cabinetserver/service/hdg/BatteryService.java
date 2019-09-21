package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryService {
    @Autowired
    BatteryMapper batteryMapper;

    public Battery findByCode(String code){
        return batteryMapper.findByCode(code);
    };
}
