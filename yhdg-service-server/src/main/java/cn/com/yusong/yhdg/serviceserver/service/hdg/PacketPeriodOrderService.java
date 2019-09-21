package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

@Service
public class PacketPeriodOrderService  extends AbstractService {
    final static Logger log = LogManager.getLogger(PacketPeriodOrderService.class);

    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public void used() {
        log.debug("套餐状态未使用->已使用更新开始...");
        int limit = 1000;

        while (true) {
            if(packetPeriodOrderMapper.updateUsedOrder(PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.USED.getValue(), new Date(), limit) < limit) {
                break;
            }
        }
        log.debug("套餐状态未使用->已使用更新结束...");
    }

    public void expire() {
        log.debug("套餐状态已使用->已过期更新开始...");
        int limit = 1000;

        while (true) {
            if(packetPeriodOrderMapper.updateExpiredOrder(PacketPeriodOrder.Status.USED.getValue(), PacketPeriodOrder.Status.EXPIRED.getValue(), new Date(), limit) < limit) {
                break;
            }
        }
        log.debug("套餐状态已使用->已过期更新结束...");
    }

    public void willExpirePush() {
        int expireDay = Integer.valueOf(systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.PACKET_PERIOD_ORDER_EXPIRE.getValue()));
        int dayInterval = Integer.valueOf(systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.PACKET_PERIOD_ORDER_EXPIRE_iNTERVAL.getValue()));

        //查询到期时间
        //Date nowDate = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(new Date(), 1), Calendar.DAY_OF_MONTH),-1);
        Date nowDate = new Date();
        Date expireDate = DateUtils.addDays(new Date(), expireDay);

        log.debug("要到期的套餐推送...");
        int  offset = 0,limit = 1000;

        Map <Integer, Integer> agentMap = new HashMap <Integer, Integer> ();

        int sum = 0;
        while (true) {
            List<PacketPeriodOrder> orderList = packetPeriodOrderMapper.findExpireList(expireDate, offset,  limit);
            if(orderList.isEmpty()){
                break;
            }

            for(PacketPeriodOrder packetPeriodOrder : orderList){
                //如果推送时间加上间隔时间大于当前时间，无需推送
                Date expireNoticeTime = packetPeriodOrder.getExpireNoticeTime();
//                if(expireNoticeTime != null && DateUtils.addDays(expireNoticeTime,dayInterval).compareTo(nowDate) > 0){
//                    continue;
//                }

                //推送客户
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceType(PushMessage.SourceType.PACKET_PERIOD_ORDER_EXPIRE.getValue());
                pushMetaData.setSourceId(packetPeriodOrder.getId());
                pushMetaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(pushMetaData);

                if(agentMap.get(packetPeriodOrder.getAgentId()) == null){
                    agentMap.put(packetPeriodOrder.getAgentId(),1);
                }else{
                    agentMap.put(packetPeriodOrder.getAgentId(),agentMap.get(packetPeriodOrder.getAgentId()) + 1);
                }

                //更新套餐
                //packetPeriodOrderMapper.updateExpireNoticeTime(packetPeriodOrder.getId(), nowDate);
            }

            offset += limit;
        }


        //推送运营商
        if(!agentMap.isEmpty()){
            for(Integer agentId : agentMap.keySet()){
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceType(PushMessage.SourceType.PACKET_PERIOD_ORDER_EXPIRE_NOTICE_AGENT.getValue());
                pushMetaData.setSourceId(String.format("%d:%d", agentId, agentMap.get(agentId)));
                pushMetaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(pushMetaData);
            }
        }

        log.debug("要到期的套餐推送结束...");
    }
}
