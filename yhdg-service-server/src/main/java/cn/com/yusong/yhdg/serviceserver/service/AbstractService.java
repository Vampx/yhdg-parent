package cn.com.yusong.yhdg.serviceserver.service;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.FaultLogMapper;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    OrderIdMapper orderIdMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PushMessageTemplateMapper pushMessageTemplateMapper;
    @Autowired
    MpPushMessageTemplateMapper mpPushMessageTemplateMapper;
    @Autowired
    FwPushMessageTemplateMapper fwPushMessageTemplateMapper;
    @Autowired
    MpPushMessageTemplateDetailMapper mpPushMessageTemplateDetailMapper;
    @Autowired
    FwPushMessageTemplateDetailMapper fwPushMessageTemplateDetailMapper;
    @Autowired
    AgentNoticeMessageMapper agentNoticeMessageMapper;
    @Autowired
    VoiceMessageTemplateMapper voiceMessageTemplateMapper;
    @Autowired
    CustomerCouponTicketGiftMapper customerCouponTicketGiftMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    protected VoiceConfigMapper voiceConfigMapper;

    public String newOrderId(OrderId.OrderIdType type) {
        Calendar calendar = new GregorianCalendar();
        int suffix = calendar.get(Calendar.YEAR);
        OrderId orderId = new OrderId(type, suffix);
        orderIdMapper.insert(orderId);
        long id = orderId.getId();

        return newOrderId(id, calendar, type);
    }

    public void insertPushMetaData(Integer sourceType, String sourceId) {
        PushMetaData data = new PushMetaData();
        data.setSourceType(sourceType);
        data.setSourceId(sourceId);
        data.setCreateTime(new Date());
        pushMetaDataMapper.insert(data);
    }

    public FaultLog insertFaultLog(Integer faultLevel, String orderId, Integer provinceId, Integer cityId, Integer districtId, Long dispatcherId, Integer agentId, String agentName, String batteryId, String cabinetId, String cabinetName,
                                   String cabinetAddress,
                                   String boxNum,
                                   Integer faultType,
                                   String faultContent) {
        FaultLog data = new FaultLog();
        data.setFaultLevel(faultLevel);
        data.setOrderId(orderId);
        data.setProvinceId(provinceId);
        data.setCityId(cityId);
        data.setDistrictId(districtId);
        data.setDispatcherId(dispatcherId);
        data.setAgentId(agentId);
        data.setAgentName(agentName);
        data.setBatteryId(batteryId);
        data.setCabinetId(cabinetId);
        data.setCabinetName(cabinetName);
        data.setCabinetAddress(cabinetAddress);
        data.setBoxNum(boxNum);
        data.setFaultType(faultType);
        data.setFaultContent(faultContent);
        data.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        data.setCreateTime(new Date());
        faultLogMapper.create(data);
        return data;
    }

    //**插入 运营商 通知消息 列表
    protected void insertAgentNoticeMessage(String title, String content, Integer agentId, int type) {
        AgentNoticeMessage agentNoticeMessage = new AgentNoticeMessage();
        agentNoticeMessage.setType(type);
        agentNoticeMessage.setTitle(title);
        agentNoticeMessage.setAgentId(agentId);
        agentNoticeMessage.setContent(content);
        agentNoticeMessage.setCreateTime(new Date());
        agentNoticeMessageMapper.insert(agentNoticeMessage);
    }

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
        if (agentInfo != null) {
            return agentInfo;
        }
        agentInfo = agentMapper.find(id);
        if (agentInfo != null) {
            memCachedClient.set(key, agentInfo, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return agentInfo;
    }

    public PushMessageTemplate findPushMessageTemplate(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_PUSH_MESSAGE_TEMPLATE, id);
        PushMessageTemplate pushMessageTemplate = (PushMessageTemplate) memCachedClient.get(key);
        if (pushMessageTemplate != null) {
            return pushMessageTemplate;
        }
        pushMessageTemplate = pushMessageTemplateMapper.find(id);
        if (pushMessageTemplate != null) {
            memCachedClient.set(key, pushMessageTemplate, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return pushMessageTemplate;
    }

    public MpPushMessageTemplate findMpPushMessageTemplate(int appId, int id) {

        MpPushMessageTemplate template = mpPushMessageTemplateMapper.find(appId, id);
        List<MpPushMessageTemplateDetail> detailList = mpPushMessageTemplateDetailMapper.findByTemplateId(appId, id);
        for (MpPushMessageTemplateDetail detail : detailList) {
            template.colorMap.put(detail.getId(), detail.getColor());
        }

        return template;
    }

    public FwPushMessageTemplate findFwPushMessageTemplate(int appId, int id) {

        FwPushMessageTemplate template = fwPushMessageTemplateMapper.find(appId, id);
        List<FwPushMessageTemplateDetail> detailList = fwPushMessageTemplateDetailMapper.findByTemplateId(appId, id);
        for (FwPushMessageTemplateDetail detail : detailList) {
            template.colorMap.put(detail.getId(), detail.getColor());
        }

        return template;
    }

    public List<MpPushMessageTemplateDetail> findMpPushMessageTemplateDetailList(int weixinmpId, int templateId) {
        String key = CacheKey.key(CacheKey.K_TEMPLATE_ID_V_MP_PUSH_MESSAGE_TEMPLATE_DETAIL_LIST, weixinmpId, templateId);

        List<MpPushMessageTemplateDetail> pushMessageTemplateDetailList = (List<MpPushMessageTemplateDetail>) memCachedClient.get(key);
        if (pushMessageTemplateDetailList != null) {
            return pushMessageTemplateDetailList;
        }

        pushMessageTemplateDetailList = mpPushMessageTemplateDetailMapper.findByTemplateId(weixinmpId, templateId);
        if (!pushMessageTemplateDetailList.isEmpty()) {
            memCachedClient.set(key, pushMessageTemplateDetailList, MemCachedConfig.CACHE_ONE_WEEK);
        }

        return pushMessageTemplateDetailList;
    }

    public List<FwPushMessageTemplateDetail> findFwPushMessageTemplateDetailList(int alipayfwId, int templateId) {

        String key = CacheKey.key(CacheKey.K_TEMPLATE_ID_V_FW_PUSH_MESSAGE_FW_TEMPLATE_DETAIL_LIST, alipayfwId, templateId);

        List<FwPushMessageTemplateDetail> pushMessageFwTemplateDetailList = (List<FwPushMessageTemplateDetail>) memCachedClient.get(key);
        if (pushMessageFwTemplateDetailList != null) {
            return pushMessageFwTemplateDetailList;
        }

        pushMessageFwTemplateDetailList = fwPushMessageTemplateDetailMapper.findByTemplateId(alipayfwId, templateId);
        if (!pushMessageFwTemplateDetailList.isEmpty()) {
            memCachedClient.set(key, pushMessageFwTemplateDetailList, MemCachedConfig.CACHE_ONE_WEEK);
        }

        return pushMessageFwTemplateDetailList;
    }

    public VoiceMessageTemplate findVoiceMessageTemplate(int id, int partnerId) {
        String key = CacheKey.key(CacheKey.K_ID_V_VOICE_MESSAGE_TEMPLATE, id, partnerId);
        VoiceMessageTemplate voiceMessageTemplate = (VoiceMessageTemplate) memCachedClient.get(key);
        if (voiceMessageTemplate != null) {
            return voiceMessageTemplate;
        }
        voiceMessageTemplate = voiceMessageTemplateMapper.find(id, partnerId);
        if (voiceMessageTemplate != null) {
            memCachedClient.set(key, voiceMessageTemplate, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return voiceMessageTemplate;
    }

    /**
     * 获取语音消息配置信息
     * @param agentId
     * @return
     */
    protected VoiceConfig getVoiceConfig(int agentId) {
        String key = CacheKey.key(CacheKey.K_ID_V_VOICE_CONFIG, agentId);
        List<VoiceConfig> list = (List<VoiceConfig>) memCachedClient.get(key);
        if (list == null) {
            list = voiceConfigMapper.findByAgent(agentId);
        }

        if (list == null || list.isEmpty()) {
           return null;
        }

        VoiceConfig config = list.get(0);
        if (list.size() > 1) {
            Random random = new Random();
            config = list.get(random.nextInt(list.size()));
        }

        return config;
    }


    /**
     * 赠送优惠券
     */

    public void giveCouponTicket(Integer category, Integer type, int agentId, int ticketType, String customerMobile,Long id) {

        CustomerCouponTicketGift ticketGift = null;
        //ticketGift = customerCouponTicketGiftMapper.find(agentId, type, category);
        ticketGift = customerCouponTicketGiftMapper.findById(id);
        Agent agent = agentMapper.find(agentId);

        if (ticketGift != null && ticketGift.getIsActive() == ConstEnum.Flag.TRUE.getValue() && agent != null) {
            CustomerCouponTicket cct = new CustomerCouponTicket();
            cct.setPartnerId(agent.getPartnerId());
            cct.setTicketName(ticketGift.getTypeName());
            cct.setMoney(ticketGift.getMoney());
            cct.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
            cct.setSourceId(null);
            cct.setSourceType(null);
            cct.setCategory(category);

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

}
