package cn.com.yusong.yhdg.routeserver.service;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.routeserver.persistence.basic.SystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    SystemConfigMapper systemConfigMapper;

//    public String findConfigValue(String id) {
//        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, id);
//        String value = (String) memCachedClient.get(key);
//        if (value != null) {
//            return value;
//        }
//        value = systemConfigMapper.findConfigValue(id);
//        if (value != null) {
//            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
//        }
//        return value;
//    }
}
