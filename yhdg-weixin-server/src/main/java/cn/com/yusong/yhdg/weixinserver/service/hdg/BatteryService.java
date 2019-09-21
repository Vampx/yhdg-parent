package cn.com.yusong.yhdg.weixinserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.weixinserver.persistence.hdg.BatteryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryService {
    @Autowired
    BatteryMapper batteryMapper;

    public Battery findByShellCode(String shellCode) {
        return batteryMapper.findByShellCode(shellCode);
    }
}
