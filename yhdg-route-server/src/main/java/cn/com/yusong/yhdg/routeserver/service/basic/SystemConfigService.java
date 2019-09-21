package cn.com.yusong.yhdg.routeserver.service.basic;

import cn.com.yusong.yhdg.routeserver.persistence.basic.SystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService {

    @Autowired
    SystemConfigMapper systemConfigMapper;

    public String findConfigValue(String id) {
        return systemConfigMapper.findConfigValue(id);
    }
}
