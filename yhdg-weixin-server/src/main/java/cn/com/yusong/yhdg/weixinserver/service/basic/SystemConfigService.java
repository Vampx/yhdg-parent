package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.SystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigService {

    @Autowired
    SystemConfigMapper systemConfigMapper;

    public List<SystemConfig> findAll() {
        return systemConfigMapper.findAll();
    }


}
