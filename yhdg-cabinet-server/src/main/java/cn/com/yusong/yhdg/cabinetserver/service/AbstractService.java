package cn.com.yusong.yhdg.cabinetserver.service;

import cn.com.yusong.yhdg.cabinetserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.basic.OrderIdMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.BatteryReportLogMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetChargerReportMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetReportBatteryMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetReportMapper;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    OrderIdMapper orderIdMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    BatteryReportLogMapper batteryReportLogMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CabinetReportMapper cabinetReportMapper;
    @Autowired
    CabinetReportBatteryMapper cabinetReportBatteryMapper;
    @Autowired
    CabinetChargerReportMapper cabinetChargerReportMapper;

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

    public boolean findTable(String tableName) {
        String key = CacheKey.key(CacheKey.K_NANE_V_TABLE, tableName);
        Integer result = (Integer) memCachedClient.get(key);
        if (result != null) {
            return true;
        }

        String table = batteryReportLogMapper.findTable(tableName);
        if (StringUtils.isNotEmpty(table)) {
            memCachedClient.set(key, 1, MemCachedConfig.CACHE_ONE_DAY);
            return true;
        }
        return false;
    }
    public boolean findCabinetChargerReportTable(String tableName) {
        String key = CacheKey.key(CacheKey.K_NANE_V_TABLE, tableName);
        Integer result = (Integer) memCachedClient.get(key);
        if (result != null) {
            return true;
        }

        String table = cabinetChargerReportMapper.findTable(tableName);
        if (StringUtils.isNotEmpty(table)) {
            memCachedClient.set(key, 1, MemCachedConfig.CACHE_ONE_DAY);
            return true;
        }
        return false;
    }
    public boolean findCabinetReportTable(String tableName) {
        String key = CacheKey.key(CacheKey.K_NANE_V_TABLE, tableName);
        Integer result = (Integer) memCachedClient.get(key);
        if (result != null) {
            return true;
        }

        String table = cabinetReportMapper.findTable(tableName);
        if (StringUtils.isNotEmpty(table)) {
            memCachedClient.set(key, 1, MemCachedConfig.CACHE_ONE_DAY);
            return true;
        }
        return false;
    }
    public boolean findCabinetReportBatteryTable(String tableName) {
        String key = CacheKey.key(CacheKey.K_NANE_V_TABLE, tableName);
        Integer result = (Integer) memCachedClient.get(key);
        if (result != null) {
            return true;
        }

        String table = cabinetReportBatteryMapper.findTable(tableName);
        if (StringUtils.isNotEmpty(table)) {
            memCachedClient.set(key, 1, MemCachedConfig.CACHE_ONE_DAY);
            return true;
        }
        return false;
    }
    public String newOrderId(OrderId.OrderIdType type) {
        Calendar calendar = new GregorianCalendar();
        int suffix = calendar.get(Calendar.YEAR);
        OrderId orderId = new OrderId(type, suffix);
        orderIdMapper.insert(orderId);
        long id = orderId.getId();

        return newOrderId(id, calendar, type);
    }

    public List<? extends AreaEntity> setAreaProperties(AreaCache areaCache, List<? extends AreaEntity> list) {
        for (AreaEntity site : list) {
            setAreaProperties(areaCache, site);
        }

        return list;
    }

    public AreaEntity setAreaProperties(AreaCache areaCache, AreaEntity areaEntity) {
        if (areaEntity == null) {
            return null;
        }

        if (areaEntity.getProvinceId() != null) {
            Area area = areaCache.get(areaEntity.getProvinceId());
            if (area != null) {
                areaEntity.setProvinceName(area.getAreaName());
            }
        }
        if (areaEntity.getCityId() != null) {
            Area area = areaCache.get(areaEntity.getCityId());
            if (area != null) {
                areaEntity.setCityName(area.getAreaName());
            }
        }
        if (areaEntity.getDistrictId() != null) {
            Area area = areaCache.get(areaEntity.getDistrictId());
            if (area != null) {
                areaEntity.setDistrictName(area.getAreaName());
            }
        }

        return areaEntity;
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
