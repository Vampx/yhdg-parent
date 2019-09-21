package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.staticserver.persistence.basic.SystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService {
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public String findConfigValue(String id) {
        return systemConfigMapper.findConfigValue(id);
    }

    public SystemConfig find(String id){
        return systemConfigMapper.find(id);
    }

}
