package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigService {
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public String findConfigValue(String id) {
        return systemConfigMapper.findConfigValue(id);
    }

    public List<SystemConfig> findAll() {
        return systemConfigMapper.findAll();
    }
}
