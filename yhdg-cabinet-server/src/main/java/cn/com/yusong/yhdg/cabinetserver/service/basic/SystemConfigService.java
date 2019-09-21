package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.cabinetserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SystemConfigService extends AbstractService {
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public String findConfigValue(String id) {
        return super.findConfigValue(id);
    }
}
