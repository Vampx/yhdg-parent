package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemConfigService {

    @Autowired
    SystemConfigMapper systemConfigMapper;

    public String findConfigValue(String id) {
        return systemConfigMapper.findConfigValue(id);
    }

    public Map<String,String> findMap(){
        List<SystemConfig> list = systemConfigMapper.find();
        Map<String,String> map = new HashMap<String, String>(list.size());
        for (SystemConfig systemConfig : list){
            map.put(systemConfig.getId(),systemConfig.getConfigValue());
        }
        return  map;
    }
}
