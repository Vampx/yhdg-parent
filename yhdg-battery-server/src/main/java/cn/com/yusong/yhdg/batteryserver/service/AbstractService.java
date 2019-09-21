package cn.com.yusong.yhdg.batteryserver.service;

import cn.com.yusong.yhdg.batteryserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.basic.CustomerInstallmentRecordPayDetailMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.hdg.BatteryReportLogMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    BatteryReportLogMapper batteryReportLogMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;


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

    public long getRentExpireTime(Battery battery, long customerId){
        Long expierTime = null;

        //先从缓存中获取，缓存没有再查询订单表
        String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, customerId);
        expierTime  = (Long) memCachedClient.get(key);
        if(expierTime == null){
            Date endTime = null;

            //如果存在分期支付未支付，并且时间到期，结束时间为到期时间
            CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = customerInstallmentRecordPayDetailMapper.findByCustomerId(customerId, CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), ConstEnum.Category.RENT.getValue(), new Date());
            if(customerInstallmentRecordPayDetail != null){
                endTime = customerInstallmentRecordPayDetail.getExpireTime();
            }else{
                //查询存在最后结束时间的订单
                RentPeriodOrder lastOrder = rentPeriodOrderMapper.findLastEndTime(customerId);
                if(lastOrder != null){
                    endTime = lastOrder.getEndTime();
                }

                List<RentPeriodOrder>  rentPeriodOrderList = rentPeriodOrderMapper.findListByCustomerIdAndStatus(customerId, RentPeriodOrder.Status.NOT_USE.getValue());
                //查询未使用套餐
                for(RentPeriodOrder rentPeriodOrder : rentPeriodOrderList){
                    if(endTime == null || endTime.compareTo(new Date()) < 0 ){
                        endTime = new Date();
                    }
                    endTime = DateUtils.addDays(endTime, rentPeriodOrder.getDayCount());
                }
            }

            //租期加1天
            if(endTime != null){
                endTime = DateUtils.addDays(endTime, 1);
                expierTime = endTime.getTime() /1000;
            }else{
                expierTime = 0l;
            }
            memCachedClient.set(key, expierTime, MemCachedConfig.CACHE_TWO_HOUR / 12);
        }
        return expierTime;

    }
}
