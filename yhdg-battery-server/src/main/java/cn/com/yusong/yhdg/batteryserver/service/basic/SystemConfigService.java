package cn.com.yusong.yhdg.batteryserver.service.basic;

import cn.com.yusong.yhdg.batteryserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService {
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public SystemConfig find(String id) {
        return systemConfigMapper.find(id);
    }
}
