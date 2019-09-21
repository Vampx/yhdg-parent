package cn.com.yusong.yhdg.vehicleserver.service;

import cn.com.yusong.yhdg.vehicleserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.vehicleserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    SystemConfigMapper systemConfigMapper;

    @Autowired
    AgentMapper agentMapper;

    public AgentInfo findAgentInfo(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, id);
        AgentInfo agentInfo = (AgentInfo) memCachedClient.get(key);
        if (agentInfo != null) {
            return agentInfo;
        }
        agentInfo = agentMapper.find(id);
        if (agentInfo != null) {
            memCachedClient.set(key, agentInfo, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return agentInfo;
    }

    public String findConfigValue(String id) {
        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, id);
        String value = (String ) memCachedClient.get(key);
        if(value != null) {
            return value;
        }

        value = systemConfigMapper.findConfigValue(id);
        if(value == null) {
            return value;
        }

        memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_DAY);
        return value;
    }
}
