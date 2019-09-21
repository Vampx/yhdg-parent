package cn.com.yusong.yhdg.serviceserver.service.basic;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.push.PushMsg;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;

import cn.com.yusong.yhdg.serviceserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * 推送
 */
@Service
public class InsertPushMessageService extends AbstractService {
    static Logger log = LogManager.getLogger(InsertPushMessageService.class);

    @Autowired
    PushMessageMapper pushMessageMapper;
    @Autowired
    PushMessageContentMapper pushMessageContentMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    CustomerDepositOrderMapper customerDepositOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CustomerNoticeMessageMapper customerNoticeMessageMapper;
    @Autowired
    UserNoticeMessageMapper userNoticeMessageMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatteryParameterMapper batteryParameterMapper;
    @Autowired
    BatteryChargeRecordMapper batteryChargeRecordMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PublicNoticeMapper publicNoticeMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    WeixinmpTemplateMessageMapper weixinmpTemplateMessageMapper;
    @Autowired
    AlipayfwTemplateMessageMapper alipayfwTemplateMessageMapper;
    @Autowired
    WeixinmpOpenIdMapper weixinmpOpenIdMapper;
    @Autowired
    AlipayfwOpenIdMapper alipayfwOpenIdMapper;
    @Autowired
    FaultFeedbackMapper faultFeedbackMapper;
    @Autowired
    AgentDayStatsMapper agentDayStatsMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    private VoiceMessageMapper voiceMessageMapper;
    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
    @Autowired
    CustomerRefundRecordMapper customerRefundRecordMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;


    /**
     * 插入微信推送消息
     */
    public void insertWeixinMessage(Customer customer, String[] variable, Integer templateId, Integer sourceType, String sourceId, String mpOpenId, String mobile) {

        if (customer.getAgentId() == null) {
            log.warn("mobile {} agentId is null", mobile);
            return;
        }

        Agent agent = agentMapper.find(customer.getAgentId());
        if(agent == null) {
            log.warn("mobile {} agent is null", mobile);
            return;
        }

        if (agent.getWeixinmpId() == null) {
            log.warn("mobile {} weixinmpId is null", mobile);
            return;
        }

        WeixinmpOpenId weixinmpOpenId = weixinmpOpenIdMapper.findByOpenId(agent.getWeixinmpId(), mpOpenId);
        if(weixinmpOpenId == null) {
            log.warn("mobile {} weixinmpOpenId is null", mobile);
            return;
        }

        mpOpenId = weixinmpOpenId.getSecondOpenId();

        if(StringUtils.isEmpty(mpOpenId)) {
            return;
        }

        WeixinmpTemplateMessage weixinMessage = new WeixinmpTemplateMessage();
        weixinMessage.setSourceType(sourceType);
        weixinMessage.setType(templateId);
        weixinMessage.setVariable(AppUtils.encodeJson2(MpPushMessageTemplate.toDetailMap(variable, findMpPushMessageTemplateDetailList(agent.getWeixinmpId(), templateId))));
        weixinMessage.setStatus(WeixinmpTemplateMessage.MessageStatus.NOT.getValue());
        weixinMessage.setDelay(0);
        weixinMessage.setCreateTime(new Date());
        weixinMessage.setSourceId(sourceId);
        weixinMessage.setWeixinmpId(agent.getWeixinmpId());
        weixinMessage.setOpenId(mpOpenId);
        weixinMessage.setMobile(mobile);
        weixinMessage.setReadCount(0);
        AppConfig config = SpringContextHolder.getBean(AppConfig.class);
        if(sourceType == PushMessage.SourceType.PACKET_PERIOD_ORDER_EXPIRE.getValue()){
            weixinMessage.setUrl(WeixinmpTemplateMessage.periodOrderExpireUrl(agent.getWeixinmpId(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl()));
        }
        if(sourceType == PushMessage.SourceType.PACKET_PERIOD_ORDER_EXPIRE_NOTICE_AGENT.getValue()){
            weixinMessage.setUrl(WeixinmpTemplateMessage.periodOrderExpireAgentUrl(customer.getUserId(),  agent.getId(), agent.getWeixinmpId(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl()));
        }
        if(sourceType == PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW.getValue()){
            weixinMessage.setUrl(WeixinmpTemplateMessage.batteryUrl(agent.getWeixinmpId(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl()));
        }
        if(sourceType == PushMessage.SourceType.CUSTOMER_GET_COUPON_TICKET.getValue()
                || sourceType == PushMessage.SourceType.CUSTOMER_COUPON_TICKET_EXPIRE.getValue()){
            weixinMessage.setUrl(WeixinmpTemplateMessage.couponTicketUrl(agent.getId(), agent.getWeixinmpId(), customer.getCategory(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl()));
        }
        if(sourceType == PushMessage.SourceType.FAULT_FLAG_BATTERY.getValue()
                || sourceType == PushMessage.SourceType.CABINET_BATTERY_REPORT_FALUT.getValue()
                || sourceType == PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT.getValue()){//异常标注电池
            weixinMessage.setUrl(WeixinmpTemplateMessage.batteryAgentUrl(customer.getUserId(), agent.getId(), agent.getWeixinmpId(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl(), sourceId));
        }
        if( sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_5.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_6.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_7.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_8.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_11.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_12.getValue()
                || sourceType == PushMessage.SourceType.FAULT_TYPE_CODE_12.getValue()
                || sourceType == PushMessage.SourceType.VOL_DIFF_HIGH.getValue()
                || sourceType == PushMessage.SourceType.SIGH_VOL_LOW.getValue()){
            weixinMessage.setUrl(WeixinmpTemplateMessage.batteryListAgentUrl(customer.getUserId(), agent.getId(), agent.getWeixinmpId(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl(), PushMessage.SourceType.getSourceType(sourceType).getType()));
        }
        if(sourceType == PushMessage.SourceType.CUSTOMER_BATTERY_OVERTIME.getValue()){
            weixinMessage.setUrl(WeixinmpTemplateMessage.batteryOverTimeUrl(customer.getUserId(), agent.getId(), agent.getWeixinmpId(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl()));
        }
        if(sourceType == PushMessage.SourceType.UNBIND__BATTERY_OUT_OVERTIME.getValue()){
            weixinMessage.setUrl(WeixinmpTemplateMessage.unbindBatteryOutOverTimeUrl(customer.getUserId(), agent.getId(), agent.getWeixinmpId(), customer.getMpOpenId(), weixinmpOpenId.getSecondOpenId(), config.getWeixinUrl()));
        }
        weixinmpTemplateMessageMapper.insert(weixinMessage);
    }

    /**
     * 插入生活号推送消息
     */
    public void insertAlipayfwMessage(Customer customer, String[] variable, Integer templateId, Integer sourceType, String sourceId, String fwOpenId, String mobile) {
        if (customer.getAgentId() == null) {
            log.warn("mobile {} agentId is null", mobile);
            return;
        }

        Agent agent = agentMapper.find(customer.getAgentId());
        if(agent == null) {
            log.warn("mobile {} agent is null", mobile);
            return;
        }

        if (agent.getAlipayfwId() == null) {
            log.warn("mobile {} AlipayfwId is null", mobile);
            return;
        }

        AlipayfwOpenId alipayfwOpenId = alipayfwOpenIdMapper.findByOpenId(agent.getAlipayfwId(), fwOpenId);
        if(alipayfwOpenId == null) {
            log.warn("mobile {} AlipayfwOpenId is null", mobile);
            return;
        }
        fwOpenId = alipayfwOpenId.getSecondOpenId();

        if(StringUtils.isEmpty(fwOpenId)) {
            return;
        }

        AlipayfwTemplateMessage alipayfwTemplateMessage = new AlipayfwTemplateMessage();
        alipayfwTemplateMessage.setSourceType(sourceType);
        alipayfwTemplateMessage.setType(templateId);
        alipayfwTemplateMessage.setVariable(AppUtils.encodeJson2(FwPushMessageTemplate.toDetailMap(variable, findFwPushMessageTemplateDetailList(agent.getAlipayfwId(), templateId))));
        alipayfwTemplateMessage.setStatus(AlipayfwTemplateMessage.MessageStatus.NOT.getValue());
        alipayfwTemplateMessage.setDelay(0);
        alipayfwTemplateMessage.setCreateTime(new Date());
        alipayfwTemplateMessage.setSourceId(sourceId);
        alipayfwTemplateMessage.setAlipayfwId(agent.getAlipayfwId());
        alipayfwTemplateMessage.setOpenId(fwOpenId);
        alipayfwTemplateMessage.setMobile(mobile);
        alipayfwTemplateMessage.setReadCount(0);
        alipayfwTemplateMessageMapper.insert(alipayfwTemplateMessage);

    }

    /**
     * 插入发送所需数据 删除元数据
     */
    @Transactional(rollbackFor = Throwable.class)
    public void insertPushMessage(PushMetaData pushMetaData, Integer agentId, String target, PushMsg.Type type, PushMsg.Data data) {
        PushMsg pushMsg = new PushMsg();
        pushMsg.setType(type.getValue());
        pushMsg.setData(data);

        PushMessage message = new PushMessage();
        message.setCreateTime(new Date());
        message.setSendStatus(PushMessage.MessageStatus.NOT.getValue());
        message.setSourceId(pushMetaData.getSourceId());
        message.setSourceType(pushMetaData.getSourceType());
        message.setAgentId(agentId);
        pushMessageMapper.insert(message);

        PushMessageContent pushMessageContent = new PushMessageContent();
        pushMessageContent.setId(message.getId());
        pushMessageContent.setTarget(target);
        pushMessageContent.setContent(YhdgUtils.encodeJson2(pushMsg));
        pushMessageContentMapper.insert(pushMessageContent);

        pushMetaDataMapper.delete(pushMetaData.getId());


    }

    //**插入 客户通知消息 列表
    protected void insertCustomerNoticeMessage(String title, String content,
                                               long customerId, String mobile, String fullname, int type) {
        CustomerNoticeMessage customerNoticeMessage = new CustomerNoticeMessage();
        customerNoticeMessage.setType(type);//暂定写死
        customerNoticeMessage.setTitle(title);
        customerNoticeMessage.setContent(content);
        customerNoticeMessage.setCustomerFullname(fullname);
        customerNoticeMessage.setCustomerMobile(mobile);
        customerNoticeMessage.setCustomerId(customerId);
        customerNoticeMessage.setCreateTime(new Date());
        customerNoticeMessageMapper.insert(customerNoticeMessage);
    }

    //**插入 用户 通知消息 列表
    protected void insertUserNoticeMessage(String title, String content,
                                           long userId, String mobile, String fullname, int type) {
        UserNoticeMessage userNoticeMessage = new UserNoticeMessage();
        userNoticeMessage.setType(type);//暂定写死
        userNoticeMessage.setTitle(title);
        userNoticeMessage.setContent(content);
        userNoticeMessage.setUserFullname(fullname);
        userNoticeMessage.setUserMobile(mobile);
        userNoticeMessage.setUserId(userId);
        userNoticeMessage.setCreateTime(new Date());
        userNoticeMessageMapper.insert(userNoticeMessage);
    }



    protected void handlePushMetaData(List<PushMetaData> pushMetaDataList) {
        for (PushMetaData metaData : pushMetaDataList) {
            try {
                //客户充值成功
                if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_DEPOSIT_SUCCESS.getValue()) {
                    insertCustomerDepositOrderPushMessage(metaData);
                }
                //客户押金支付成功
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue()) {
                    insertCustomerForegiftOrderPushMessage(metaData);
                }
                //客户套餐支付成功
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue()) {
                    insertPacketPeriodOrderPushMessage(metaData);
                }
                //客户优惠券
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_GET_COUPON_TICKET.getValue()) {
                    insertCustomerGetCouponTicketPushMessage(metaData);
                }
                //客户申请押金退款成功
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS.getValue()) {
                    insertCustomerForegiftRefundOrderPushMessage(metaData);
                }
                //租金到期推送
                else if (metaData.getSourceType() == PushMessage.SourceType.PACKET_PERIOD_ORDER_EXPIRE.getValue()) {
                    insertPacketPeriodOrderExpirePushMessage(metaData);
                }
                //分期付到期推送
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_INSTALLMENT_EXPIRE.getValue()) {
                    //insertCustomerInstallmentExpirePushMessage(metaData);
                }
                //优惠券到期推送
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_COUPON_TICKET_EXPIRE.getValue()) {
                    insertCustomerGetCouponTicketExpirePushMessage(metaData);
                }
                //客户租电押金支付成功
                else if (metaData.getSourceType() == PushMessage.SourceType.RENT_FOREGIFT_PAY_SUCCESS.getValue()) {
                    insertRentForegiftOrderPushMessage(metaData);
                }
                //客户租电套餐支付成功
                else if (metaData.getSourceType() == PushMessage.SourceType.RENT_PERIOD_ORDER_PAY_SUCCESS.getValue()) {
                    insertRentPeriodOrderPushMessage(metaData);
                }

                //客户打开满电箱门
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_OPEN_NEW_BATTER_BOX.getValue()) {
                    insertCustomerOpenNewBatterboxPushMessage(metaData);
                }
                //换电订单未取超时
                else if (metaData.getSourceType() == PushMessage.SourceType.BATTERY_ORDER_NOT_TAKE_TIMEOUT.getValue()) {
                    insertCustomerNotTakeTimeoutPushMessage(metaData);
                }
                //客户电池入柜异常
                else if (metaData.getSourceType() == PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue()) {
                    insertBatteryInBoxNoticePushMessage(metaData);
                }
                //骑手未关门通知
                else if (metaData.getSourceType() == PushMessage.SourceType.NO_CLOSE_BOX.getValue()) {
                    insertNoCloseBoxPushMessage(metaData);
                }
                //客户电池电量低
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW.getValue()) {
                    insertCustomerBatteryVolumeLowPushMessage(metaData);
                    insertCustomerBatteryVolumeLowVoiceMessage(metaData);
                }
                //电量低推送运营商
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT.getValue()) {
                    //insertAgentBatteryVolumeLowPushMessage(metaData);
                }
                //电池发生保护 插入运营商告警
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_5.getValue()) {
                    insertAgentFaultTypeCode5Message(metaData);
                }
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_6.getValue()) {
                    insertAgentFaultTypeCode6Message(metaData);
                }
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_7.getValue()) {
                    insertAgentFaultTypeCode7Message(metaData);
                }
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_8.getValue()) {
                    insertAgentFaultTypeCode8Message(metaData);
                }
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue() ) {
                    insertAgentFaultTypeCode9Message(metaData);
                }
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue()) {
                    insertAgentFaultTypeCode10Message(metaData);
                }
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_11.getValue() ) {
                    insertAgentFaultTypeCode11Message(metaData);
                }
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_TYPE_CODE_12.getValue() ) {
                    insertAgentFaultTypeCode12Message(metaData);
                }
                //压差过大异常
                else if (metaData.getSourceType() == PushMessage.SourceType.VOL_DIFF_HIGH.getValue()) {
                    insertAgentVolDiffHighMessage(metaData);
                }
                //骑手租赁电池超时异常
                else if (metaData.getSourceType() == PushMessage.SourceType.CUSTOMER_BATTERY_OVERTIME.getValue()) {
                    insertCustomerBatteryOverTimeMessage(metaData);
                }
                //未绑定电池在外超时异常
                else if (metaData.getSourceType() == PushMessage.SourceType.UNBIND__BATTERY_OUT_OVERTIME.getValue()) {
                    insertUnbindBatteryOverTimeMessage(metaData);
                }
                //电池单体电压小于最小电压断电
                else if (metaData.getSourceType() == PushMessage.SourceType.SIGH_VOL_LOW.getValue()) {
                    insertSignVolLowPushMessage(metaData);
                }
                //异常标注电池
                else if (metaData.getSourceType() == PushMessage.SourceType.FAULT_FLAG_BATTERY.getValue()) {
                    insertFaultFlagBatteryPushMessage(metaData);
                }
                //换电柜离线
                else if (metaData.getSourceType() == PushMessage.SourceType.CABINET_OFFLINE.getValue()) {
                    insertCabinetOfflinePushMessage(metaData);
                }
                //换电柜高温报警
                else if (metaData.getSourceType() == PushMessage.SourceType.CABINET_HIGH_TEMP.getValue()) {
                    insertCabinetHotTempPushMessage(metaData);
                }
                //换电柜低温报警
                else if (metaData.getSourceType() == PushMessage.SourceType.CABINET_LOW_TEMP.getValue()) {
                    insertCabinetLowTempPushMessage(metaData);
                }
                //骑手换电未取通知
                else if (metaData.getSourceType() == PushMessage.SourceType.BATTERY_ORDER_NOT_TAKE_TIMEOUT_AGENT.getValue()) {
                    insertCustomerNotTakeTimeoutAgentPushMessage(metaData);
                }
                //骑手未关门通知
                else if (metaData.getSourceType() == PushMessage.SourceType.NO_CLOSE_BOX_NOTICE_AGENT.getValue()) {
                    insertNoCloseBoxAgentPushMessage(metaData);
                }
                //换电柜电池通讯异常
                else if (metaData.getSourceType() == PushMessage.SourceType.CABINET_BATTERY_REPORT_FALUT.getValue()) {
                    insertCabinetBatteryFalutPushMessage(metaData);
                }
                //烟雾传感器报警
                else if (metaData.getSourceType() == PushMessage.SourceType.SMOKE_ALARM.getValue()) {
                    //烟雾传感器报警 插入运营商告警
                    insertAgentFaultSmokeAlarmMessage(metaData);
                }
                //运营商日结算
                else if (metaData.getSourceType() == PushMessage.SourceType.AGENT_SETTLEMENT_NOTICE.getValue()) {
                    insertAgentSettlementMessage(metaData);
                }
                //租期到期通知运营商
                else if (metaData.getSourceType() == PushMessage.SourceType.PACKET_PERIOD_ORDER_EXPIRE_NOTICE_AGENT.getValue()) {
                    insertAgentPacketPeriodOrderExpirePushMessage(metaData);
                }

            } catch (Exception e) {
                log.error("handlePushMetaData data", String.format("sourceType:%d,sourceId:%s",metaData.getSourceType(),metaData.getSourceId()));
                log.error("handlePushMetaData error", e);
            }finally {
                pushMetaDataMapper.delete(metaData.getId());
            }
        }
    }

    public void insertCustomerGetCouponTicketPushMessage(PushMetaData metaData) {
        String errorMsg = "赠送客户优惠券推送=>";
        CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(Long.parseLong(metaData.getSourceId()));
        if (customerCouponTicket == null) {
            log.error("{} 赠送优惠券ID:{},不存在", errorMsg, metaData.getSourceId());
            return;
        }
        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}赠送优惠券模板为空", errorMsg);
            return;
        }
        if (customerCouponTicket.getAgentId() == null) {
            log.error("{}优惠券所属运营商是空", customerCouponTicket.getAgentId());
            return;
        }
        Agent agent = agentMapper.find(customerCouponTicket.getAgentId());
        if (agent == null) {
            log.error("{}优惠券所属运营商是空", customerCouponTicket.getAgentId());
            return;
        }

        Customer customer = customerMapper.findByMobile(agent.getPartnerId(), customerCouponTicket.getCustomerMobile());
        if (customer == null) {
            log.error("{}  客户手机:{},不存在", errorMsg, customerCouponTicket.getCustomerMobile());
            return;
        }
        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${fullname}", "${ticketName}", "${money}", "${expireDate}"},
                new String[]{customer.getFullname(), customerCouponTicket.getTicketName(), String.format("%.2f", 1d * customerCouponTicket.getMoney() / 100.0), DateFormatUtils.format(customerCouponTicket.getExpireTime(), Constant.DATE_FORMAT)}));
        data.setIsPlay(template.getIsPlay());

        insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        if (customer.getLoginType() != null) {
            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "agentName" , agent.getAgentName(),
                        "money" ,String.format("%.2f", 1d * customerCouponTicket.getMoney() / 100.0),
                        "ticketName",CustomerCouponTicket.Category.getName(customerCouponTicket.getCategory()) + CustomerCouponTicket.TicketType.getName(customerCouponTicket.getTicketType()),
                        "customerName", customerCouponTicket.getCustomerMobile(),
                        "beginDate", DateFormatUtils.format(customerCouponTicket.getCreateTime(), Constant.DATE_TIME_FORMAT),
                        "endDate", DateFormatUtils.format(customerCouponTicket.getExpireTime(), Constant.DATE_TIME_FORMAT)
                };
                customer.setCategory(customerCouponTicket.getCategory());
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.COUPON_TICKET_NOTICE.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertCustomerGetCouponTicketExpirePushMessage(PushMetaData metaData) {
        String errorMsg = "客户优惠券到期推送=>";

        CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(Long.parseLong(metaData.getSourceId()));
        if (customerCouponTicket == null) {
            log.error("{} 赠送优惠券ID:{},不存在", errorMsg, metaData.getSourceId());
            return;
        }
        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}赠送优惠券模板为空", errorMsg);
            return;
        }
        if (customerCouponTicket.getAgentId() == null) {
            log.error("{}优惠券所属运营商是空", customerCouponTicket.getAgentId());
            return;
        }
        Agent agent = agentMapper.find(customerCouponTicket.getAgentId());
        if (agent == null) {
            log.error("{}优惠券所属运营商是空", customerCouponTicket.getAgentId());
            return;
        }

        Customer customer = customerMapper.findByMobile(agent.getPartnerId(), customerCouponTicket.getCustomerMobile());
        if (customer == null) {
            log.error("{}  客户手机:{},不存在", errorMsg, customerCouponTicket.getCustomerMobile());
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${fullname}", "${expireTime}"},
                new String[]{customer.getFullname(), DateFormatUtils.format(customerCouponTicket.getExpireTime(), Constant.DATE_FORMAT)}));
        data.setIsPlay(template.getIsPlay());

        insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        if (customer.getLoginType() != null) {
            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "name", CustomerCouponTicket.Category.getName(customerCouponTicket.getCategory()) + CustomerCouponTicket.TicketType.getName(customerCouponTicket.getTicketType()),
                        "date", DateFormatUtils.format(customerCouponTicket.getExpireTime(), Constant.DATE_TIME_FORMAT),
                        "agentName",agent.getAgentName()
                };
                customer.setCategory(customerCouponTicket.getCategory());
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.COUPON_TICKET_EXPIRE.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertCustomerOpenNewBatterboxPushMessage(PushMetaData metaData) {
        String errorMsg = "客户打开满电箱门推送=>";
        BatteryOrder order = batteryOrderMapper.find(metaData.getSourceId());
        if (order == null) {
            log.error("{}，电池订单为空 id:{}", errorMsg, metaData.getSourceId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户打开满电箱门模板为空");
            return;
        }
        Customer customer = customerMapper.find(order.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, order.getCustomerId());
            return;
        }
        if (customer.getLoginType() != null) {
            PushMsg.Data data = new PushMsg.Data();
            data.setEventTime(new Date());
            data.setTitle(template.getTitle());
            data.setSourceType(metaData.getSourceType());
            data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${fullname}", "${cabinetName}", "${boxNum}"},
                    new String[]{customer.getFullname(), order.getTakeCabinetName(), order.getTakeBoxNum()}));
            data.setIsPlay(template.getIsPlay());
            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                log.debug("客户打开满电箱门即将推送APP");
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                    insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                            customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());
                } else {
                    log.error("{} 客户ID:{},token没有注册 ", errorMsg, order.getCustomerId());
                    return;
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                log.debug("客户打开满电箱门即将推送公众号");
                insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                        customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

                String[] variable = {
                        "name", customer.getFullname(),
                        "cabinetName", order.getTakeCabinetName() + "/" + order.getTakeBoxNum(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_OPEN_NEW_BATTER_BOX.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            } else if (ConstEnum.ClientType.FW.getValue() == customer.getLoginType() && customer.getFwOpenId() != null) {
                log.debug("客户打开满电箱门即将推送生活号");
                insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                        customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

                String[] variable = {
                        "name", customer.getFullname(),
                        "cabinetName", order.getTakeCabinetName(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                insertAlipayfwMessage(customer, variable, ConstEnum.AppPushMessageFwTemplateType.CUSTOMER_OPEN_NEW_BATTER_BOX.getValue(), AlipayfwTemplateMessage.SourceType.SYSTEM.getValue(), metaData.getSourceId(), customer.getFwOpenId(), customer.getMobile());

            }
        }
    }

    public void insertCustomerNotTakeTimeoutPushMessage(PushMetaData metaData) {
        String errorMsg = "换电订单未取超时=>";
        BatteryOrder batteryOrder = batteryOrderMapper.find(metaData.getSourceId());
        if (batteryOrder == null) {
            log.error("{} 换电订单不存在 id:{} ", errorMsg, metaData.getSourceId());
            return;
        }
        Cabinet cabinet = cabinetMapper.find(batteryOrder.getTakeCabinetId());
        if (cabinet == null) {
            log.error("{}  换电柜不存在:{}", errorMsg, batteryOrder.getTakeCabinetId());
            return;
        }

        Customer customer = customerMapper.find(batteryOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  换电订单ID:{},没有找到所属客户", errorMsg, batteryOrder.getId());

            return;
        }
        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:换电订单未取超时没有配置模板", errorMsg);
            return;
        }

        if (customer.getLoginType() != null) {
            PushMsg.Data data = new PushMsg.Data();
            data.setEventTime(new Date());
            data.setTitle(template.getTitle());
            data.setSourceType(metaData.getSourceType());
            data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${orderId}", "${cabinetId}", "${cabinetName}", "${batteryId}", "${address}"},
                    new String[]{batteryOrder.getId(), cabinet.getId(), cabinet.getCabinetName(), batteryOrder.getBatteryId(), cabinet.getAddress()}));
            data.setIsPlay(template.getIsPlay());

            insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                    customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "orderId", batteryOrder.getId(),
                        "cabinetName", batteryOrder.getTakeCabinetName(),
                        "boxNum", batteryOrder.getTakeBoxNum(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.BATTERY_ORDER_NOT_TAKE_TIMEOUT_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertCustomerBatteryVolumeLowPushMessage(PushMetaData metaData) {
        String errorMsg = "电池电量低推送=>";
        Battery battery = batteryMapper.find(metaData.getSourceId());
        if (battery == null) {
            log.error("{} 电池 id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户电池电量低模板没有配置", errorMsg);

            return;
        }

        BatteryOrder batteryOrder = batteryOrderMapper.find(battery.getOrderId());
        if(batteryOrder == null){
            log.error("{}  电池ID:{},没有找到对应订单", errorMsg, battery.getId());
            return;
        }

        Customer customer = customerMapper.find(batteryOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, battery.getId());
            return;
        }

        if (customer.getLoginType() != null) {
            PushMsg.Data data = new PushMsg.Data();
            data.setEventTime(new Date());
            data.setTitle(template.getTitle());
            data.setSourceType(metaData.getSourceType());
            data.setContent(StringUtils.replaceEach(template.getContent(),
                    new String[]{"${fullname}", "${batteryId}", "${volume}"},
                    new String[]{customer.getFullname(), battery.getId(), battery.getVolume() + "%"}));
            data.setIsPlay(template.getIsPlay());

            insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                    customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }

            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName", batteryOrder.getTakeCabinetName(),
                        "fullName", customer.getFullname(),
                        "volume", battery.getVolume() + "",
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_BATTERY_VOLUME_LOW_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertAgentBatteryVolumeLowPushMessage(PushMetaData metaData) {
        String errorMsg = "运营商电池电量低推送=>";
        Battery battery = batteryMapper.find(metaData.getSourceId());
        if (battery == null) {
            log.error("{} 电池 id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户电池电量低模板没有配置", errorMsg);

            return;
        }

        BatteryOrder batteryOrder = batteryOrderMapper.find(battery.getOrderId());
        if(batteryOrder == null){
            log.error("{}  电池ID:{},没有找到对应订单", errorMsg, battery.getId());
            return;
        }

        Customer customer = customerMapper.find(batteryOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, battery.getId());
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(customer.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", customer.getAgentId());
            return;
        }

        List<User> userList = userMapper.findByAgent(batteryOrder.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(customer.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName", batteryOrder.getTakeCabinetName(),
                        "fullName", customer.getFullname(),
                        "volume", battery.getVolume() + "",
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentVolDiffHighMessage(PushMetaData metaData) {
        String errorMsg = "压差过大异常 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_17.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.VOL_DIFF_HIGH_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.VOL_DIFF_HIGH_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.VOL_DIFF_HIGH_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }

    }

    public void insertSignVolLowPushMessage(PushMetaData metaData) {
        String errorMsg = "电池单体电压小于最小电压断电 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_20.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.SIGH_VOL_LOW_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.SIGH_VOL_LOW_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.SIGH_VOL_LOW_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }

    }

    public void insertFaultFlagBatteryPushMessage(PushMetaData metaData) {
        String errorMsg = "异常标注电池 插入运营商告警";
        Battery battery = batteryMapper.find(metaData.getSourceId());
        if (battery == null) {
            log.error("{} 电池不存在 id:{} ", errorMsg, metaData.getSourceId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:异常标注电池板没有配置", errorMsg);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}","${cabinetName}","${boxNum}"}, new String[]{battery.getId(), battery.getCabinetName(), battery.getBoxNum()}));
        data.setIsPlay(template.getIsPlay());

        //查看要推送的运营商
        Agent agent = agentMapper.find(battery.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", battery.getAgentId());
            return;
        }

        insertAgentNoticeMessage(data.getTitle(), data.getContent(), battery.getAgentId(), AgentNoticeMessage.Type.DANGER.getValue());

        List<User> userList = userMapper.findByAgent(battery.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_FLAG_BATTERY_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "batteryId",battery.getId(),
                        "cabinetName", battery.getCabinetName(),
                        "boxNum", battery.getBoxNum(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_FLAG_BATTERY_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }

    }

    public void insertCabinetBatteryFalutPushMessage(PushMetaData metaData) {
        String errorMsg = "换电柜电池通讯异常 插入运营商告警";
        String [] sourceIds = metaData.getSourceId().split(":");
        String cabinetId = sourceIds[0];
        String boxNum = sourceIds[1];
        String batteryId = sourceIds[2];

        Battery battery = batteryMapper.find(batteryId);
        if (battery == null) {
            log.error("{} 电池不存在 id:{} ", errorMsg, metaData.getSourceId());
            return;
        }

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if (cabinet == null) {
            log.error("{} 柜子不存在 id:{} ", errorMsg, metaData.getSourceId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:换电柜电池通讯异常没有配置", errorMsg);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}","${cabinetName}","${boxNum}"}, new String[]{battery.getId(), cabinet.getCabinetName(), boxNum}));
        data.setIsPlay(template.getIsPlay());

        //查看要推送的运营商
        Agent agent = agentMapper.find(battery.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", battery.getAgentId());
            return;
        }

        insertAgentNoticeMessage(data.getTitle(), data.getContent(), battery.getAgentId(), AgentNoticeMessage.Type.DANGER.getValue());


        int batteryFalutCount = faultLogMapper.findTotalCountByAgentAndTypeAndBattery(battery.getAgentId(), FaultLog.FaultType.CODE_25.getValue(), batteryId, null, null);
        int boxNumFalutCount = faultLogMapper.findTotalCountByAgentAndTypeAndBattery(battery.getAgentId(), FaultLog.FaultType.CODE_25.getValue(), null, cabinetId, boxNum);


        List<User> userList = userMapper.findByAgent(battery.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.CABINET_BATTERY_REPORT_FALUT_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName", cabinet.getCabinetName(),
                        "boxNum", boxNum,
                        "batteryId", batteryId,
                        "batteryFalutCount", String.format("%d", batteryFalutCount),
                        "boxNumFalutCount", String.format("%d", boxNumFalutCount),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.CABINET_BATTERY_REPORT_FALUT_FAULT.getValue(), metaData.getSourceType(), batteryId, agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }

    }

    public void insertCustomerBatteryOverTimeMessage(PushMetaData metaData) {
        String errorMsg = "骑手租赁电池超时  插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);
        Integer count = Integer.parseInt(sourceIds[1]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 骑手租赁电池超时没有配置", errorMsg);

            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null", agentId);
            return;
        }

        String no ="0001";
        try {
            Date subDate = DateUtils.parseDate( DateFormatUtils.format(new Date(),Constant.DATE_FORMAT) + " 12:00:00", new String[] {Constant.DATE_TIME_FORMAT});
            if(new Date().compareTo(subDate) > 0){
                no ="0002";
            }
        } catch (ParseException e) {
            log.error("error {}", e);
        }

        Integer timeout = Integer.valueOf(findConfigValue(ConstEnum.SystemConfigKey.BATTERY_BINDING_TIME.getValue()));
        if(timeout == null){
            timeout = 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - timeout);
        String beginTime = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd HH:mm") + ":00";
        String endTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm") + ":00";
        int overHours = timeout * 24;
        String agentName = agent.getAgentName();

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.CUSTOMER_BATTERY_OVERTIME_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "count",String.format("%d",count),
                        "orderId", DateFormatUtils.format(new Date(), "yyyyMMdd") + no,
                        "beginTime",beginTime,
                        "endTime",endTime,
                        "hour",overHours + "",
                        "agentName",agentName
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_BATTERY_OVERTIME_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertUnbindBatteryOverTimeMessage(PushMetaData metaData) {
        String errorMsg = "未绑定电池在外超时异常  插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);
        Integer count = Integer.parseInt(sourceIds[1]);
        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:未绑定电池在外超时异常没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null", agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        List<User> userList = userMapper.findByAgent(agent.getId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.UNBIND__BATTERY_OUT_OVERTIME_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.UNBIND__BATTERY_OUT_OVERTIME_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.UNBIND__BATTERY_OUT_OVERTIME_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }

    }

    public void insertCustomerInstallmentExpirePushMessage(PushMetaData metaData) {
        String errorMsg = "分期付到期推送=>";
        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = customerInstallmentRecordPayDetailMapper.find(Long.parseLong(metaData.getSourceId()));
        if (customerInstallmentRecordPayDetail == null) {
            log.error("{}分期 id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }

        Customer customer = customerMapper.find(customerInstallmentRecordPayDetail.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg,customerInstallmentRecordPayDetail.getCustomerId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 分期付到期到期模板没有配置", errorMsg);

            return;
        }

        if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
            String title = "分期付通知";
            String content = String.format("您【%s】存在未支付分期订单，为了不影响您的用电，请在截止时间%s前续费",customer.getFullname(), DateFormatUtils.format(customerInstallmentRecordPayDetail.getExpireTime(), Constant.DATE_FORMAT));
            insertCustomerNoticeMessage(title, content, customer.getId(),
                    customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

            String[] variable = {
                    "id",  String.format("第%d期 %d",customerInstallmentRecordPayDetail.getNum(),customerInstallmentRecordPayDetail.getId()),
                    "date", DateFormatUtils.format(customerInstallmentRecordPayDetail.getExpireTime(), Constant.DATE_FORMAT),
                    "content", "您【"+customer.getFullname()+"】存在未支付分期订单，为了不影响您的用电，请尽快续租，避免影响您的使用"
            };
            insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.PACKET_PERIOD_ORDER_EXPIRE_AGENT.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
        }

    }

    public void insertPacketPeriodOrderExpirePushMessage(PushMetaData metaData) {
        String errorMsg = "租金到期推送=>";
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(metaData.getSourceId());
        if (packetPeriodOrder == null) {
            log.error("{}租金 id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }

        Customer customer = customerMapper.find(packetPeriodOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg,packetPeriodOrder.getCustomerId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 租金到期模板没有配置", errorMsg);

            return;
        }

        Agent agent = agentMapper.find(customer.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", customer.getAgentId());
            return;
        }

        if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
            String title = "续租通知";
            String content = String.format("电池将在<span style='color:#FF5055'>%s</span>后（%s）到期，为了不影响您的用电，请及时续费",AppUtils.formatTimeUnit((packetPeriodOrder.getEndTime().getTime() - System.currentTimeMillis()) / 1000),DateFormatUtils.format(packetPeriodOrder.getEndTime(), Constant.DATE_FORMAT));
            insertCustomerNoticeMessage(title, content, customer.getId(),
                    customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

            String cabinetName = "";
            if(packetPeriodOrder.getCabinetId() != null){
                cabinetName = cabinetMapper.find(packetPeriodOrder.getCabinetId() ).getCabinetName();
            }
            String[] variable = {
                    "id", packetPeriodOrder.getId(),
                    "date", DateFormatUtils.format(packetPeriodOrder.getEndTime(), Constant.DATE_TIME_FORMAT),
                    "cabinetName", cabinetName,
                    "customerName",customer.getFullname()
            };
            insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.PACKET_PERIOD_ORDER_EXPIRE.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
        }

    }

    public void insertAgentPacketPeriodOrderExpirePushMessage(PushMetaData metaData) {
        String errorMsg = "运营商租金到期推送=>";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);
        Integer count = Integer.parseInt(sourceIds[1]);


        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 租金到期模板没有配置", errorMsg);

            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null", agentId);
            return;
        }

        String no ="0001";
        try {
            Date subDate = DateUtils.parseDate( DateFormatUtils.format(new Date(),Constant.DATE_FORMAT) + " 12:00:00", new String[] {Constant.DATE_TIME_FORMAT});
            if(new Date().compareTo(subDate) > 0){
                no ="0002";
            }
        } catch (ParseException e) {
            log.error("error {}", e);
        }

        String date = DateFormatUtils.format(DateUtils.addDays(new Date(), 2),Constant.DATE_FORMAT);
        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.PACKET_PERIOD_ORDER_EXPIRE_AGENT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "count",String.format("%d",count),
                        "id", DateFormatUtils.format(new Date(), "yyyyMMdd") + no,
                        "date", date
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.PACKET_PERIOD_ORDER_EXPIRE_AGENT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }

    }

    public void insertCabinetNotExistEmptyBoxPushMessage(PushMetaData metaData) {
        String errorMsg = "柜子没有空箱推送=>";
        Cabinet cabinet = cabinetMapper.find(metaData.getSourceId());
        if (cabinet == null) {
            log.error("{} {}  柜子不存在", errorMsg, metaData.getSourceId());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:柜子没有空箱模板没有配置", errorMsg);

            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${cabinetId}", "${cabinetName}"},
                new String[]{cabinet.getId(), cabinet.getCabinetName()}));
        data.setIsPlay(template.getIsPlay());

        //柜子没有空箱 插入运营商故障告警
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), cabinet.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        /* if (cabinet.getDispatcherId() == null) {
            log.error("{}  柜子名称:{},没有负责的调度人员!", errorMsg, cabinet.getCabinetName());

            return;
        }*/

        User user = userMapper.find(cabinet.getDispatcherId());
        if (user == null) {
            log.error("{}  柜子Id:{},调度员（User）不存在,userId:{}", cabinet.getId(), errorMsg, cabinet.getDispatcherId());

            return;
        }

        if (StringUtils.isNotEmpty(user.getPushToken()) && user.getPushType() != null) {
            insertPushMessage(metaData, null, String.valueOf(user.getId()), PushMsg.Type.USER, data);
            insertUserNoticeMessage(data.getTitle(), data.getContent(), user.getId(),
                    user.getMobile(), user.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());
        } else {
            log.error("{} 调度员token没有注册UserId:{}", errorMsg, user.getId());

            return;
        }
    }

    public void insertCabinetOfflinePushMessage(PushMetaData metaData) {
        String errorMsg = "换电柜离线推送";
        Cabinet cabinet = cabinetMapper.find(metaData.getSourceId());
        if (cabinet == null) {
            log.error("{} 换电柜id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }
        if (cabinet.getIsOnline() == ConstEnum.Flag.FALSE.getValue()) {

            if (StringUtils.isEmpty(cabinet.getId())) {
                log.error("{}  换电柜名称:{} {},没有设置所属柜子!", errorMsg, cabinet.getId(), cabinet.getCabinetName());

                return;
            }

            PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
            if (template == null) {
                log.error("{}:柜子离线模板没有配置", errorMsg);

                return;
            }

            Agent agent = agentMapper.find(cabinet.getAgentId());
            if(agent == null) {
                log.warn("id {} agent is null", cabinet.getAgentId());
                return;
            }

            PushMsg.Data data = new PushMsg.Data();
            data.setEventTime(new Date());
            data.setTitle(template.getTitle());
            data.setSourceType(metaData.getSourceType());
            data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${cabinetId}", "${cabinetName}"},
                    new String[]{cabinet.getId(), cabinet.getCabinetName()}));
            data.setIsPlay(template.getIsPlay());

            //换电柜离线 插入运营商故障告警
            insertAgentNoticeMessage(data.getTitle(), data.getContent(), cabinet.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

            List<User> userList = userMapper.findByAgent(cabinet.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.CABINET_OFFLINE_FAULT.getValue(), agent.getWeixinmpId());
            for(User user : userList){
                Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
                if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                    String[] variable = {
                            "cabinetName", cabinet.getCabinetName(),
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                    };
                    agentCustomer.setUserId(user.getId());
                    insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.CABINET_OFFLINE_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
                }
            }
        }
    }

    public void insertCustomerDepositOrderPushMessage(PushMetaData metaData) {
        String errorMsg = "客户充值推送";
        CustomerDepositOrder order = customerDepositOrderMapper.find(metaData.getSourceId());
        if (order == null) {
            log.error("{} 充值订单为空 id:{}", errorMsg, metaData.getSourceId());

            return;
        }
        if (order.getStatus() == CustomerDepositOrder.Status.OK.getValue()) {
            PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
            if (template == null) {
                log.error("{} 客户充值金额模板为空", errorMsg);

                return;
            }
            Customer customer = customerMapper.find(order.getCustomerId());
            if (customer == null) {
                log.error("{} 客户ID:{},不存在", errorMsg, order.getCustomerId());

                return;
            }
            if (customer.getLoginType() != null) {
                PushMsg.Data data = new PushMsg.Data();
                data.setEventTime(new Date());
                data.setTitle(template.getTitle());
                data.setSourceType(metaData.getSourceType());
                data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${fullname}", "${money}", "${gift}", "${balance}"},
                        new String[]{customer.getFullname(), String.format("%.2f", 1d * order.getMoney() / 100.0),
                                String.format("%.2f", 1d * order.getGift() / 100.0), String.format("%.2f", 1d * customer.getBalance() / 100.0)}));
                data.setIsPlay(template.getIsPlay());

                insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                        customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

                if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                    log.debug("客户充值即将推送APP");
                    if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                        insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                    } else {
                        log.error("{} 客户token没有注册 customerId:{}", errorMsg, customer.getId());

                        return;
                    }
                } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                    log.debug("客户充值即将推送公众号");
                    String[] variable = {
                            "money", String.format("%.2f", 1d * order.getMoney() / 100.0),
                            "gift", String.format("%.2f", 1d * order.getGift() / 100.0),
                            "balance", String.format("%.2f", 1d * customer.getBalance() / 100.0),
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT),
                    };
                    insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_DEPOSIT_SUCCESS.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
                }/* else if (ConstEnum.ClientType.FW.getValue() == customer.getLoginType() && customer.getFwOpenId() != null) {
                    log.debug("客户充值即将推送生活号");
                    String[] variable = {
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT),
                            "money", String.format("%.2f", 1d * order.getMoney() / 100.0),
                            "gift", String.format("%.2f", 1d * order.getGift() / 100.0),
                            "balance", String.format("%.2f", 1d * customer.getBalance() / 100.0)
                    };
                    insertAlipayfwMessage(customer, variable, ConstEnum.AppPushMessageFwTemplateType.CUSTOMER_DEPOSIT_SUCCESS.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getFwOpenId(), customer.getMobile());
                }*/

            }
        }
    }

    public void insertAgentFaultTypeCode5Message(PushMetaData metaData) {
        String errorMsg = "充电过温发生保护 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());


        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_5.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_5_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_5_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_5_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultTypeCode6Message(PushMetaData metaData) {
        String errorMsg = "充电低温异常 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_6.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_6_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_6_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_6_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultTypeCode7Message(PushMetaData metaData) {
        String errorMsg = "放电高温异常 插入运营商告警";
        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_7.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_7_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_7_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_7_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultTypeCode8Message(PushMetaData metaData) {
        String errorMsg = "放电低温异常 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_8.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_8_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_8_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_8_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultTypeCode9Message(PushMetaData metaData) {
        String errorMsg = "充电过流异常 插入运营商告警";
        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_9.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(),ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_9_FAULT.getValue(),agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_9_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_9_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultTypeCode10Message(PushMetaData metaData) {
        String errorMsg = "放电过流异常 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_10.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_10_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_10_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_10_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultTypeCode11Message(PushMetaData metaData) {
        String errorMsg = "短路保护异常 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${batteryId}"}, new String[]{sourceIds[1]}));
        data.setIsPlay(template.getIsPlay());
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentId, AgentNoticeMessage.Type.DANGER.getValue());

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_11.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_11_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_11_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_11_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultTypeCode12Message(PushMetaData metaData) {
        String errorMsg = "IC错误异常 插入运营商告警";

        String [] sourceIds = metaData.getSourceId().split(":");
        Integer agentId = Integer.parseInt(sourceIds[0]);

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:电池发生保护模板没有配置", errorMsg);
            return;
        }

        //查看要推送的运营商
        Agent agent = agentMapper.find(agentId);
        if(agent == null) {
            log.warn("id {} agent is null",agentId);
            return;
        }

        int count = faultLogMapper.findTotalCountByAgentAndType(agentId, FaultLog.FaultType.CODE_12.getValue());

        List<User> userList = userMapper.findByAgent(agentId, User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_12_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "warnType",ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_12_FAULT.getName(),
                        "count", String.format("%d",count),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.FAULT_TYPE_CODE_12_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentFaultSmokeAlarmMessage(PushMetaData metaData) {
        String errorMsg = "烟雾传感器报警推送";
        Cabinet cabinet = cabinetMapper.find(metaData.getSourceId());
        if (cabinet == null) {
            log.error("{} 换电柜id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }


        if (StringUtils.isEmpty(cabinet.getId())) {
            log.error("{}  换电柜名称:{} {},没有设置所属柜子!", errorMsg, cabinet.getId(), cabinet.getCabinetName());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:烟雾传感器报警模板没有配置", errorMsg);

            return;
        }

        Agent agent = agentMapper.find(cabinet.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", cabinet.getAgentId());
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${cabinetId}",
                        "${cabinetName}"},
                new String[]{cabinet.getId(),
                        cabinet.getCabinetName()}));
        data.setIsPlay(template.getIsPlay());

        //烟雾传感器报警 插入运营商故障告警
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), cabinet.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        List<User> userList = userMapper.findByAgent(cabinet.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.SMOKE_ALARM_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName" ,cabinet.getCabinetName(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.SMOKE_ALARM_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertAgentSettlementMessage(PushMetaData metaData) {
        String errorMsg = "运营商日结算推送";
        //今天看昨天的结算记录
        String yesDate = DateFormatUtils.format(DateUtils.addDays(metaData.getCreateTime(), -1).getTime(), Constant.DATE_FORMAT);

        AgentDayStats agentDayStats = agentDayStatsMapper.find(Integer.valueOf(metaData.getSourceId()), yesDate, ConstEnum.Category.EXCHANGE.getValue());

        if (agentDayStats == null) {
            log.error("{} 结算记录不存在 id:{} ", errorMsg, metaData.getSourceId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:运营商日结算模板没有配置", errorMsg);
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(),
                new String[]{"${statsDate}", "${money}", "${income}", "${platformIncome}", "${provinceIncome}", "${cityIncome}"},
                new String[]{yesDate,
                        String.format("%.2f元", 1d * agentDayStats.getMoney() / 100.0),
                        String.format("%.2f元", 1d * agentDayStats.getIncome() / 100.0),
                        String.format("%.2f元", 1d * agentDayStats.getPlatformIncome() / 100.0),
                        String.format("%.2f元", 1d * agentDayStats.getProvinceIncome() / 100.0),
                        String.format("%.2f元", 1d * agentDayStats.getCityIncome() / 100.0)}));
        data.setIsPlay(template.getIsPlay());

        insertAgentNoticeMessage(data.getTitle(), data.getContent(), agentDayStats.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

    }

    public void insertCustomerForegiftOrderPushMessage(PushMetaData metaData) {
        String errorMsg = "客户押金支付成功推送";
        CustomerForegiftOrder order = customerForegiftOrderMapper.find(metaData.getSourceId());
        if (order == null) {
            log.error("{} 充值订单为空 id:{}", errorMsg, metaData.getSourceId());

            return;
        }

        if (order.getStatus() == CustomerForegiftOrder.Status.PAY_OK.getValue()) {
            PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
            if (template == null) {
                log.error("{} 客户押金支付成功模板为空", errorMsg);

                return;
            }

            Customer customer = customerMapper.find(order.getCustomerId());
            if (customer == null) {
                log.error("{} 客户ID:{},不存在", errorMsg, order.getCustomerId());

                return;
            }

            if (customer.getLoginType() != null) {
                PushMsg.Data data = new PushMsg.Data();
                data.setEventTime(new Date());
                data.setTitle(template.getTitle());
                data.setSourceType(metaData.getSourceType());
                data.setContent(StringUtils.replaceEach(template.getContent(),
                        new String[]{"${fullname}", "${money}"},
                        new String[]{customer.getFullname(), String.format("%.2f", 1d * order.getMoney() / 100.0)}));
                data.setIsPlay(template.getIsPlay());

                insertCustomerNoticeMessage(data.getTitle(),
                        data.getContent(),
                        customer.getId(),
                        customer.getMobile(),
                        customer.getFullname(),
                        PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());


                if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                    if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                        insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                    }
                } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                    String[] variable = {
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT),
                            "money", String.format("%.2f", 1d * order.getMoney() / 100.0),
                            "orderId", order.getId()
                    };
                    insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
                } /*else if (ConstEnum.ClientType.FW.getValue() == customer.getLoginType() && customer.getFwOpenId() != null) {
                    String[] variable = {
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT),
                            "money", String.format("%.2f", 1d * order.getMoney() / 100.0)
                    };
                    insertAlipayfwMessage(customer, variable, ConstEnum.AppPushMessageFwTemplateType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue(), AlipayfwTemplateMessage.SourceType.SYSTEM.getValue(), metaData.getSourceId(), customer.getFwOpenId(), customer.getMobile());
                }*/
            }
        }
    }

    public void insertRentForegiftOrderPushMessage(PushMetaData metaData) {
        String errorMsg = "客户租电押金支付成功推送";
        RentForegiftOrder order = rentForegiftOrderMapper.find(metaData.getSourceId());
        if (order == null) {
            log.error("{} 租电押金订单为空 id:{}", errorMsg, metaData.getSourceId());

            return;
        }

        if (order.getStatus() == CustomerForegiftOrder.Status.PAY_OK.getValue()) {
            PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
            if (template == null) {
                log.error("{} 客户租电押金支付成功模板为空", errorMsg);

                return;
            }

            Customer customer = customerMapper.find(order.getCustomerId());
            if (customer == null) {
                log.error("{} 客户ID:{},不存在", errorMsg, order.getCustomerId());

                return;
            }

            if (customer.getLoginType() != null) {
                PushMsg.Data data = new PushMsg.Data();
                data.setEventTime(new Date());
                data.setTitle(template.getTitle());
                data.setSourceType(metaData.getSourceType());
                data.setContent(StringUtils.replaceEach(template.getContent(),
                        new String[]{"${fullname}", "${money}"},
                        new String[]{customer.getFullname(), String.format("%.2f", 1d * order.getMoney() / 100.0)}));
                data.setIsPlay(template.getIsPlay());

                insertCustomerNoticeMessage(data.getTitle(),
                        data.getContent(),
                        customer.getId(),
                        customer.getMobile(),
                        customer.getFullname(),
                        PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());


                if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                    if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                        insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                    }
                } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                    String[] variable = {
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT),
                            "money", String.format("%.2f", 1d * order.getMoney() / 100.0),
                            "orderId", order.getId()
                    };
                    insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
                }/* else if (ConstEnum.ClientType.FW.getValue() == customer.getLoginType() && customer.getFwOpenId() != null) {
                    String[] variable = {
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT),
                            "money", String.format("%.2f", 1d * order.getMoney() / 100.0)
                    };
                    insertAlipayfwMessage(customer, variable, ConstEnum.AppPushMessageFwTemplateType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue(), AlipayfwTemplateMessage.SourceType.SYSTEM.getValue(), metaData.getSourceId(), customer.getFwOpenId(), customer.getMobile());
                }*/
            }
        }
    }

    public void insertPacketPeriodOrderPushMessage(PushMetaData metaData) {
        String errorMsg = "客户套餐支付成功推送";
        PacketPeriodOrder order = packetPeriodOrderMapper.find(metaData.getSourceId());
        if (order == null) {
            log.error("{} 客户套餐支付订单为空 id:{}", errorMsg, metaData.getSourceId());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}客户套餐支付成功模板为空", errorMsg);

            return;
        }

        Customer customer = customerMapper.find(order.getCustomerId());
        if (customer == null) {
            log.error("{} 客户ID:{},不存在", errorMsg, order.getCustomerId());

            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(),
                new String[]{"${fullname}", "${money}", "${daycount}"},
                new String[]{customer.getFullname(), String.format("%.2f", 1d * order.getMoney() / 100.0), order.getDayCount().toString()}));
        data.setIsPlay(template.getIsPlay());
        if (customer.getLoginType() != null) {
            insertCustomerNoticeMessage(data.getTitle(),
                    data.getContent(),
                    customer.getId(),
                    customer.getMobile(),
                    customer.getFullname(),
                    PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());


            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "orderId", order.getId(),
                        "date",  DateFormatUtils.format(order.getPayTime(), Constant.DATE_TIME_FORMAT),
                        "money", String.format("%.2f", 1d * order.getMoney() / 100.0),
                        "limitCount", order.getLimitCount() != null && order.getLimitCount() > 0 ? order.getLimitCount() + "次" : "不限",
                        "daycount",  String.format("%d天", order.getDayCount())
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertRentPeriodOrderPushMessage(PushMetaData metaData) {
        String errorMsg = "客户租电套餐支付成功推送";
        RentPeriodOrder order = rentPeriodOrderMapper.find(metaData.getSourceId());
        if (order == null) {
            log.error("{} 客户租电套餐订单为空 id:{}", errorMsg, metaData.getSourceId());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户租电套餐充值成功模板为空", errorMsg);

            return;
        }

        Customer customer = customerMapper.find(order.getCustomerId());
        if (customer == null) {
            log.error("{} 客户ID:{},不存在", errorMsg, order.getCustomerId());

            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(),
                new String[]{"${fullname}", "${money}", "${daycount}"},
                new String[]{customer.getFullname(), String.format("%.2f", 1d * order.getMoney() / 100.0), order.getDayCount().toString()}));
        data.setIsPlay(template.getIsPlay());
        if (customer.getLoginType() != null) {
            insertCustomerNoticeMessage(data.getTitle(),
                    data.getContent(),
                    customer.getId(),
                    customer.getMobile(),
                    customer.getFullname(),
                    PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());


            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "orderId", order.getId(),
                        "date",  DateFormatUtils.format(order.getPayTime(), Constant.DATE_TIME_FORMAT),
                        "money", String.format("%.2f", 1d * order.getMoney() / 100.0),
                        "limitCount", "不限",
                        "daycount",  String.format("%d天", order.getDayCount())
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertCustomerForegiftRefundOrderPushMessage(PushMetaData metaData) {
        String errorMsg = "客户申请退款成功推送";
        List<CustomerRefundRecord> refundRecordList = customerRefundRecordMapper.findListByorderId(metaData.getSourceId());
        if (refundRecordList.size() == 0) {
            log.error("{} 退款订单为空 id:{}", errorMsg, metaData.getSourceId());
            return;
        }

        int money = 0;
        Long customerId  = null;
        for(CustomerRefundRecord customerRefundRecord : refundRecordList){
            money += customerRefundRecord.getRefundMoney();
            customerId = customerRefundRecord.getCustomerId();

        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户申请退款成功模板为空", errorMsg);
            return;
        }

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            log.error("{} 客户ID:{},不存在", errorMsg,customerId);
            return;
        }

        if (customer.getLoginType() != null) {
            PushMsg.Data data = new PushMsg.Data();
            data.setEventTime(new Date());
            data.setTitle(template.getTitle());
            data.setSourceType(metaData.getSourceType());
            data.setContent(StringUtils.replaceEach(template.getContent(),
                    new String[]{"${fullname}"},
                    new String[]{customer.getFullname()}));
            data.setIsPlay(template.getIsPlay());

            insertCustomerNoticeMessage(data.getTitle(),
                    data.getContent(),
                    customer.getId(),
                    customer.getMobile(),
                    customer.getFullname(),
                    PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());


            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "reason", "无",
                        "money", String.format("%.2f", 1d * money / 100.0)
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            } else if (ConstEnum.ClientType.FW.getValue() == customer.getLoginType() && customer.getFwOpenId() != null) {
                String[] variable = {
                        "reason", "无",
                        "money", String.format("%.2f", 1d * money / 100.0)
                };
                insertAlipayfwMessage(customer, variable, ConstEnum.AppPushMessageFwTemplateType.CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS.getValue(), AlipayfwTemplateMessage.SourceType.SYSTEM.getValue(), metaData.getSourceId(), customer.getFwOpenId(), customer.getMobile());
            }
        }

    }

    public void insertCabinetHotTempPushMessage(PushMetaData metaData) {
        String errorMsg = "换电柜温度过高推送";
        Cabinet cabinet = cabinetMapper.find(metaData.getSourceId());
        if (cabinet == null) {
            log.error("{} 换电柜id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }

        if (StringUtils.isEmpty(cabinet.getId())) {
            log.error("{}  换电柜名称:{} {},没有设置所属柜子!", errorMsg, cabinet.getId(), cabinet.getCabinetName());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:换电柜温度过高模板没有配置", errorMsg);

            return;
        }


        Agent agent = agentMapper.find(cabinet.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", cabinet.getAgentId());
            return;
        }

        int temp = 0;
        if (cabinet.getTemp1() == null && cabinet.getTemp2() == null) {
            log.error("{}  换电柜id:{},温度为null", errorMsg, cabinet.getId());

            return;

        } else if (cabinet.getTemp1() != null && cabinet.getTemp2() != null) {
            temp = cabinet.getTemp1() > cabinet.getTemp2() ? cabinet.getTemp1() / 100 : cabinet.getTemp2() / 100;

        } else if (cabinet.getTemp1() != null && cabinet.getTemp2() == null) {
            temp = cabinet.getTemp1() / 100;

        } else if (cabinet.getTemp1() == null && cabinet.getTemp2() != null) {
            temp = cabinet.getTemp2() / 100;

        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${cabinetId}",
                        "${cabinetName}",
                        "${temp}"},
                new String[]{cabinet.getId(),
                        cabinet.getCabinetName(),
                        String.valueOf(temp)}));
        data.setIsPlay(template.getIsPlay());

        //换电柜温度过高 插入运营商故障告警
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), cabinet.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        AppConfig config = SpringContextHolder.getBean(AppConfig.class);

        List<User> userList = userMapper.findByAgent(cabinet.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.CABINET_HIGH_TEMP_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName",cabinet.getCabinetName(),
                        "temp", temp + "",
                        "alarmTemp", config.getCabinetHotAlarmTemp() + "",
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.CABINET_HIGH_TEMP_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }


    }

    public void insertCabinetLowTempPushMessage(PushMetaData metaData) {
        String errorMsg = "换电柜温度过过低推送";
        Cabinet cabinet = cabinetMapper.find(metaData.getSourceId());
        if (cabinet == null) {
            log.error("{} 换电柜id:{} 不存在", errorMsg, metaData.getSourceId());

            return;
        }

        if (StringUtils.isEmpty(cabinet.getId())) {
            log.error("{}  换电柜名称:{} {},没有设置所属柜子!", errorMsg, cabinet.getId(), cabinet.getCabinetName());

            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:换电柜温度过高模板没有配置", errorMsg);

            return;
        }


        Agent agent = agentMapper.find(cabinet.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", cabinet.getAgentId());
            return;
        }

        int temp = 0;
        if (cabinet.getTemp1() == null && cabinet.getTemp2() == null) {
            log.error("{}  换电柜id:{},温度为null", errorMsg, cabinet.getId());
            return;

        } else if (cabinet.getTemp1() != null && cabinet.getTemp2() != null) {
            temp = cabinet.getTemp1() > cabinet.getTemp2() ? cabinet.getTemp2() / 100 : cabinet.getTemp1() / 100;

        } else if (cabinet.getTemp1() != null && cabinet.getTemp2() == null) {
            temp = cabinet.getTemp1() / 100;

        } else if (cabinet.getTemp1() == null && cabinet.getTemp2() != null) {
            temp = cabinet.getTemp2() / 100;

        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${cabinetId}",
                        "${cabinetName}",
                        "${temp}"},
                new String[]{cabinet.getId(),
                        cabinet.getCabinetName(),
                        String.valueOf(temp)}));
        data.setIsPlay(template.getIsPlay());

        //换电柜温度过高 插入运营商故障告警
        insertAgentNoticeMessage(data.getTitle(), data.getContent(), cabinet.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        AppConfig config = SpringContextHolder.getBean(AppConfig.class);

        List<User> userList = userMapper.findByAgent(cabinet.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.CABINET_LOW_TEMP_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(agent.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName",cabinet.getCabinetName(),
                        "temp", temp + "",
                        "alarmTemp", config.getCabinetLowAlarmTemp() + "",
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.CABINET_LOW_TEMP_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }

    }

    public void insertAccountLoginAtAnotherPhonePushMessage(PushMetaData metaData) {
        String errorMsg = "账户在其他设备登录推送";
        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{}:账户在其他设备登录模板没有配置", errorMsg);

            return;
        }
        String[] split = metaData.getSourceId().split(":");
        Long customerId = Long.valueOf(split[0]);
        if (customerId == null) {
            log.error("{} 客户ID为空", errorMsg);

            return;
        }

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            log.error("{} 客户ID:{},不存在", errorMsg, customerId);

            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(template.getContent());
        data.setIsPlay(template.getIsPlay());

        insertPushMessage(metaData, null, metaData.getSourceId(), PushMsg.Type.CUSTOMER, data);
        insertCustomerNoticeMessage(data.getTitle(),
                data.getContent(),
                customer.getId(),
                customer.getMobile(),
                customer.getFullname(),
                PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());
    }

    public void insertBatteryInBoxNoticePushMessage(PushMetaData metaData) {
        String errorMsg = "电池入柜异常通知=>";

        BatteryOrder batteryOrder = batteryOrderMapper.find(metaData.getSourceId());
        if(batteryOrder == null){
            log.error("{}  换电订单不存在:{}", errorMsg, metaData.getSourceId());
            return;
        }


        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户电池入柜通知模板为空");
            return;
        }
        Customer customer = customerMapper.find(batteryOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, batteryOrder.getCustomerId());
            return;
        }
        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${cabinetName}", "${boxNum}", "${message}"},
                new String[]{batteryOrder.getPutCabinetName(), batteryOrder.getPutBoxNum(), batteryOrder.getErrorMessage()}));
        data.setIsPlay(template.getIsPlay());

        insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        if (customer.getLoginType() != null) {
            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName", batteryOrder.getTakeCabinetName(),
                        "boxNum", batteryOrder.getTakeBoxNum(),
                        "message", batteryOrder.getErrorMessage(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.BATTERY_IN_BOX_NOTICE_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertCustomerNotTakeTimeoutAgentPushMessage(PushMetaData metaData) {
        String errorMsg = "骑手换电未取通知运营商=>";

        BatteryOrder batteryOrder = batteryOrderMapper.find(metaData.getSourceId());
        if (batteryOrder == null) {
            log.error("{} 换电订单不存在 id:{} ", errorMsg, metaData.getSourceId());
            return;
        }

        Cabinet cabinet = cabinetMapper.find(batteryOrder.getTakeCabinetId());
        if (cabinet == null) {
            log.error("{}  换电柜不存在:{}", errorMsg, batteryOrder.getTakeCabinetId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户电池入柜通知运营商模板为空");
            return;
        }
        Customer customer = customerMapper.find(batteryOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, batteryOrder.getCustomerId());
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${fullname}","${cabinetId}",
                        "${cabinetName}",
                        "${batteryId}"},
                new String[]{
                        batteryOrder.getCustomerFullname(),
                        cabinet.getId(),
                        cabinet.getCabinetName(),
                        batteryOrder.getBatteryId()}));
        data.setIsPlay(template.getIsPlay());

        insertAgentNoticeMessage(data.getTitle(), data.getContent(), cabinet.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        Agent agent = agentMapper.find(cabinet.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", cabinet.getAgentId());
            return;
        }

        List<User> userList = userMapper.findByAgent(cabinet.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.BATTERY_ORDER_NOT_TAKE_TIMEOUT_AGENT_FAULT.getValue(), agent.getWeixinmpId());
        for(User user : userList){
            Customer agentCustomer = customerMapper.findByMobile(batteryOrder.getPartnerId(), user.getMobile());
            if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                String[] variable = {
                        "orderId", batteryOrder.getId(),
                        "cabinetName", cabinet.getCabinetName(),
                        "boxNum", batteryOrder.getTakeBoxNum(),
                        "fullname", batteryOrder.getCustomerFullname(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                agentCustomer.setUserId(user.getId());
                insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.BATTERY_ORDER_NOT_TAKE_TIMEOUT_AGENT_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
            }
        }
    }

    public void insertNoCloseBoxPushMessage(PushMetaData metaData) {
        String errorMsg = "骑手未关门通知=>";


        BatteryOrder batteryOrder = batteryOrderMapper.find(metaData.getSourceId());
        if(batteryOrder == null){
            log.error("{}  换电订单不存在:{}", errorMsg, metaData.getSourceId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 客户未关门通知模板为空");
            return;
        }
        Customer customer = customerMapper.find(batteryOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, batteryOrder.getCustomerId());
            return;
        }
        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${fullname}", "${cabinetName}", "${boxNum}"},
                new String[]{customer.getFullname(), batteryOrder.getTakeCabinetName(), batteryOrder.getTakeBoxNum()}));
        data.setIsPlay(template.getIsPlay());

        insertCustomerNoticeMessage(data.getTitle(), data.getContent(), customer.getId(),
                customer.getMobile(), customer.getFullname(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());

        if (customer.getLoginType() != null) {
            if (ConstEnum.ClientType.APP.getValue() == customer.getLoginType()) {
                if (StringUtils.isNotEmpty(customer.getPushToken()) && customer.getPushType() != null) {
                    insertPushMessage(metaData, null, String.valueOf(customer.getId()), PushMsg.Type.CUSTOMER, data);
                }
            } else if (ConstEnum.ClientType.MP.getValue() == customer.getLoginType() && customer.getMpOpenId() != null) {
                String[] variable = {
                        "cabinetName", batteryOrder.getTakeCabinetName(),
                        "boxNum", batteryOrder.getTakeBoxNum(),
                        "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                };
                insertWeixinMessage(customer, variable, ConstEnum.AppPushMessageTemplateType.NO_CLOSE_BOX_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), customer.getMpOpenId(), customer.getMobile());
            }
        }
    }

    public void insertNoCloseBoxAgentPushMessage(PushMetaData metaData) {
        String errorMsg = "骑手未关门通知运营商=>";


        BatteryOrder batteryOrder = batteryOrderMapper.find(metaData.getSourceId());
        if(batteryOrder == null){
            log.error("{}  换电订单不存在:{}", errorMsg, metaData.getSourceId());
            return;
        }

        PushMessageTemplate template = findPushMessageTemplate(metaData.getSourceType());
        if (template == null) {
            log.error("{} 骑手未关门通知运营商通知模板为空");
            return;
        }
        Customer customer = customerMapper.find(batteryOrder.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, batteryOrder.getCustomerId());
            return;
        }

        PushMsg.Data data = new PushMsg.Data();
        data.setEventTime(new Date());
        data.setTitle(template.getTitle());
        data.setSourceType(metaData.getSourceType());
        data.setContent(StringUtils.replaceEach(template.getContent(), new String[]{"${fullname}","${cabinetId}",
                        "${cabinetName}",
                        "${batteryId}"},
                new String[]{
                        batteryOrder.getCustomerFullname(),
                        batteryOrder.getTakeCabinetId(),
                        batteryOrder.getTakeCabinetName(),
                        batteryOrder.getBatteryId()}));
        data.setIsPlay(template.getIsPlay());

        insertAgentNoticeMessage(data.getTitle(), data.getContent(), batteryOrder.getAgentId(), PushMessage.SourceType.getSourceType(metaData.getSourceType()).getType());
        Agent agent = agentMapper.find(customer.getAgentId());
        if(agent == null) {
            log.warn("id {} agent is null", customer.getAgentId());
            return;
        }

        String content  =  String.format("站点【%s】的【%s】号格口被骑手【%s】使用后未关门，请关注",batteryOrder.getTakeCabinetName(),batteryOrder.getTakeBoxNum(),batteryOrder.getCustomerFullname());
        if(StringUtils.isNotEmpty(content)){
            List<User> userList = userMapper.findByAgent(batteryOrder.getAgentId(), User.AccountType.AGENT.getValue(), ConstEnum.AppPushMessageTemplateType.NO_CLOSE_BOX_NOTICE_AGENT_FAULT.getValue(), agent.getWeixinmpId());
            for(User user : userList){
                Customer agentCustomer = customerMapper.findByMobile(batteryOrder.getPartnerId(), user.getMobile());
                if (agentCustomer != null && agentCustomer.getLoginType() != null && ConstEnum.ClientType.MP.getValue() == agentCustomer.getLoginType() && agentCustomer.getMpOpenId() != null) {
                    String[] variable = {
                            "cabinetName", batteryOrder.getTakeCabinetName(),
                            "boxNum", batteryOrder.getTakeBoxNum(),
                            "fullname", batteryOrder.getCustomerFullname(),
                            "date", DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT)
                    };
                    agentCustomer.setUserId(user.getId());
                    insertWeixinMessage(agentCustomer, variable, ConstEnum.AppPushMessageTemplateType.NO_CLOSE_BOX_NOTICE_AGENT_FAULT.getValue(), metaData.getSourceType(), metaData.getSourceId(), agentCustomer.getMpOpenId(), agentCustomer.getMobile());
                }
            }
        }
    }

    /**
     * 插入语音消息元数据
     * @param metaData
     */
    public void insertCustomerBatteryVolumeLowVoiceMessage(PushMetaData metaData) {
        String errorMsg = "电池电量低语音消息 =>";
        Battery battery = batteryMapper.find(metaData.getSourceId());
        if (battery == null) {
            log.error("{} 电池 id:{} 不存在", errorMsg, metaData.getSourceId());
            return;
        }

        // 订单低电量语音推送时间不为空时表示已推送过
        BatteryOrder batteryOrder = batteryOrderMapper.find(battery.getOrderId());
        if (batteryOrder == null) {
            log.error("{} 换电订单 id:{} 不存在", errorMsg, battery.getOrderId());
            return;
        }

        if (batteryOrder.getLowVolumeVoiceTime() != null) {
            log.error("{} 此换电订单已发送过语音消息，订单id:{}", errorMsg, batteryOrder.getId());
            return;
        }

        //电池管理客户
        CustomerExchangeBattery customerExchangeBattery = customerExchangeBatteryMapper.findByBattery(battery.getId());
        if(customerExchangeBattery == null){
            log.error("{}  电池ID:{},没有找到所属客户", errorMsg, battery.getId());
            return;
        }

        Customer customer = customerMapper.find(customerExchangeBattery.getCustomerId());
        if (customer == null) {
            log.error("{}  客户ID:{},不存在", errorMsg, customer.getId());
            return;
        } else if(customer.getLowVolumeCount() == null || customer.getLowVolumeCount()<=0){
            log.error("{}  客户ID:{}，没有剩余次数", errorMsg, customer.getId());
            return;
        }

        VoiceMessageTemplate template = findVoiceMessageTemplate(VoiceMessageTemplate.Type.LOW_ELECTRICITY.getValue(), customer.getPartnerId());
        if (template == null) {
            log.error("{} 客户电池电量低模板没有配置", errorMsg);
            return;
        }

        if (battery.getAgentId() == null) {
            log.error("{}  运营商id不存在", errorMsg);
            return;
        }

        Agent agent = agentMapper.find(battery.getAgentId());
        if (agent == null) {
            log.error("{}  运营商:{},不存在", errorMsg, battery.getAgentId());
            return;
        }

        VoiceConfig account = getVoiceConfig(agent.getId());
        if (account == null) {
            log.error("Not VoiceConfig, agentId = {}", agent.getId());
            return;
        }

        Date now = new Date();
        VoiceMessage voiceMessage = new VoiceMessage();
        voiceMessage.setPartnerId(customer.getPartnerId());
        voiceMessage.setAgentId(battery.getAgentId());
        voiceMessage.setAgentName(agent.getAgentName());
        voiceMessage.setAgentCode(agent.getAgentCode());
        voiceMessage.setCreateTime(now);
        voiceMessage.setHandleTime(now);
        voiceMessage.setStatus(MobileMessage.MessageStatus.NOT.getValue());
        voiceMessage.setSourceId(battery.getOrderId());
        voiceMessage.setSourceType(VoiceMessage.SourceType.BATTERY_ORDER.getValue());
        voiceMessage.setCalledNumber(customer.getMobile());
        voiceMessage.setCalledShowNumber(template.getCalledShowNumber());
        voiceMessage.setVolume(template.getVolume());
        voiceMessage.setPlayTimes(template.getPlayTimes());
        voiceMessage.setContent(template.getContent());
        voiceMessage.setSenderId(null);
        voiceMessage.setType(VoiceMessageTemplate.Type.LOW_ELECTRICITY.getValue());
        voiceMessage.setDelay(0);

        Map map = new HashMap();
        map.put("name", customer.getFullname());
        String lowVolume = findConfigValue(ConstEnum.SystemConfigKey.LOW_VOLUME.getValue());
        map.put("charge", lowVolume);
        voiceMessage.setVariable(AppUtils.encodeJson2(map));
        voiceMessage.setTemplateCode(template.getCode());

        if(template.getContent().indexOf("${") >= 0) {
            log.error("mobile message error: {}", template.getContent());
        }

        voiceMessageMapper.insert(voiceMessage);
        batteryOrderMapper.updateLowVolumeVoiceTime(battery.getOrderId(), now);
        customerMapper.updateLowVolumeCount(customer.getId(), customer.getLowVolumeCount()-1);
    }
}
