package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.staticserver.persistence.basic.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService  {

    static Logger log = LogManager.getLogger(AbstractService.class);

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    OrderIdMapper orderIdMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    CustomerCouponTicketGiftMapper customerCouponTicketGiftMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    LaxinPayOrderMapper laxinPayOrderMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    LaxinSettingMapper laxinSettingMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;

    public String findConfigValue(String id) {
        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, id);
        String value = (String) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = systemConfigMapper.findConfigValue(id);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
    }

    public AgentInfo findAgentInfo(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, id);
        AgentInfo agentInfo = (AgentInfo) memCachedClient.get(key);
        if(agentInfo != null) {
            return agentInfo;
        }
        agentInfo =  agentMapper.find(id);
        if(agentInfo != null) {
            memCachedClient.set(key, agentInfo, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return agentInfo;
    }

    public String findAgentConfigValue(int agentId, String id) {
        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_CONFIG_VALUE, id, agentId);
        String value = (String) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = agentSystemConfigMapper.findConfigValue(agentId, id);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
    }

    /**
     * 支付押金租金赠送优惠券
     */
    public void giveCouponTicket(String sourceId, Integer category, Integer sourceType, Integer type, Integer dayCount, int agentId,int ticketType, String customerMobile){

        CustomerCouponTicketGift ticketGift = null;
        //押金优惠券配置唯一 租金配置可以有多个
        if (type == CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue()) {
            ticketGift = customerCouponTicketGiftMapper.find(agentId, type, category, null);
        } else {
            ticketGift = customerCouponTicketGiftMapper.find(agentId, type, category, dayCount);
        }
        Agent agent = agentMapper.find(agentId);

        if (ticketGift != null && ticketGift.getIsActive() == ConstEnum.Flag.TRUE.getValue() && agent != null) {
            CustomerCouponTicket cct = new CustomerCouponTicket();
            cct.setPartnerId(agent.getPartnerId());
            cct.setTicketName(ticketGift.getTypeName());
            cct.setMoney(ticketGift.getMoney());
            cct.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
            cct.setCategory(category);
            cct.setSourceId(sourceId);
            cct.setSourceType(sourceType);

            Date beginTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
            Date endTime = DateUtils.addSeconds(DateUtils.addDays(beginTime, ticketGift.getDayCount()), -ticketGift.getDayCount());

            cct.setAgentId(agentId);
            cct.setTicketType(ticketType);
            cct.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
            cct.setBeginTime(beginTime);
            cct.setExpireTime(endTime);
            cct.setCustomerMobile(customerMobile);
            cct.setMemo("");
            cct.setCreateTime(new Date());
            customerCouponTicketMapper.insert(cct);


            PushMetaData pushMetaData = new PushMetaData();
            pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_GET_COUPON_TICKET.getValue());
            pushMetaData.setSourceId(String.format("%d", cct.getId()));
            pushMetaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(pushMetaData);
        }

    }

    public String newOrderId(OrderId.OrderIdType type) {
        Calendar calendar = new GregorianCalendar();
        int suffix = calendar.get(Calendar.YEAR);
        OrderId orderId = new OrderId(type, suffix);
        orderIdMapper.insert(orderId);
        long id = orderId.getId();

        return newOrderId(id, calendar, type);
    }

    //这里不要加事务 由调用方法加事务
    public void handleLaxinCustomer(Agent agent, Customer customer, int foregiftMoney, int buyPacketPeriodMoney) {
        //客户买好押金后 处理拉新记录
        LaxinCustomer laxinCustomer = laxinCustomerMapper.findByTargetMobile(customer.getMobile());
        if (laxinCustomer != null && agent.getId().equals(laxinCustomer.getAgentId())) {
            if (laxinCustomer.getForegiftTime() == null) {
                Laxin laxin = laxinMapper.find(laxinCustomer.getLaxinId());
                if (laxin != null) {
                    Date now = new Date();

                    if (laxin.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
                        laxinCustomerMapper.updateByTimes(laxinCustomer.getId(), laxin.getIncomeType(), laxin.getLaxinMoney(), customer.getId(), customer.getFullname(), now);
                        int laxinExpireTime = Integer.parseInt(findConfigValue(ConstEnum.SystemConfigKey.LAXIN_EXPIRE_TIME.getValue()));
                        if (System.currentTimeMillis() - laxinCustomer.getCreateTime().getTime() <= laxinExpireTime * 24L * 3600 * 1000) {
                            LaxinRecord laxinRecord = new LaxinRecord();
                            laxinRecord.setId(newOrderId(OrderId.OrderIdType.LAXIN_RECORD));
                            laxinRecord.setAgentId(agent.getId());
                            laxinRecord.setAgentName(agent.getAgentName());
                            laxinRecord.setAgentCode(agent.getAgentCode());
                            laxinRecord.setLaxinId(laxin.getId());
                            laxinRecord.setLaxinMobile(laxin.getMobile());
                            laxinRecord.setLaxinMoney(laxin.getLaxinMoney());
                            laxinRecord.setTargetCustomerId(customer.getId());
                            laxinRecord.setTargetMobile(customer.getMobile());
                            laxinRecord.setTargetFullname(customer.getFullname());
                            laxinRecord.setStatus(LaxinRecord.Status.WAIT.getValue());
                            laxinRecord.setIncomeType(laxin.getIncomeType());
                            laxinRecord.setForegiftMoney(foregiftMoney);
                            laxinRecord.setPacketPeriodMoney(buyPacketPeriodMoney);
                            laxinRecord.setCreateTime(now);
                            laxinRecordMapper.insert(laxinRecord);
                        }


                    } else {
                        Date packetPeriodExpireTime = DateUtils.addMonths(now, laxin.getPacketPeriodMonth());
                        laxinCustomerMapper.updateByMonth(laxinCustomer.getId(), laxin.getIncomeType(), laxin.getPacketPeriodMoney(), laxin.getPacketPeriodMonth(), packetPeriodExpireTime, customer.getId(), customer.getFullname(), now);
                    }

                    Customer laxinCustomerAccount = customerMapper.findByMobile(customer.getPartnerId(), laxin.getMobile());
                    customerMapper.updateLaxinInfo(customer.getId(), laxin.getMobile(), laxinCustomerAccount == null ? "" : laxinCustomerAccount.getFullname());
                }
            }
        }

        //自动成为拉新人员(暂时就百里新能源自动拉新)
        Laxin laxin = laxinMapper.findByMobile(agent.getId(), customer.getMobile());
        List<LaxinSetting> laxinSettingList = laxinSettingMapper.findByType(agent.getId(), LaxinSetting.Type.REGISTER.getValue());
        if(laxin == null && !laxinSettingList.isEmpty()) {
            LaxinSetting setting = laxinSettingList.get(0);

            laxin = new Laxin();
            laxin.setPartnerId(agent.getPartnerId());
            laxin.setAgentId(agent.getId());
            laxin.setAgentCode(agent.getAgentCode());
            laxin.setAgentName(agent.getAgentName());
            laxin.setMobile(customer.getMobile());
            laxin.setPassword(CodecUtils.password(laxin.getPassword()));
            laxin.setLaxinMoney(setting.getLaxinMoney());
            laxin.setTicketMoney(setting.getTicketMoney());
            laxin.setTicketDayCount(setting.getTicketDayCount());
            laxin.setPacketPeriodMoney(setting.getPacketPeriodMoney());
            laxin.setPacketPeriodMonth(setting.getPacketPeriodMonth());
            laxin.setIsActive(ConstEnum.Flag.TRUE.getValue());
            laxin.setIncomeType(setting.getIncomeType());
            laxin.setIntervalDay(setting.getIntervalDay());
            laxin.setCreateTime(new Date());
            laxinMapper.insert(laxin);
        }
    }
}
