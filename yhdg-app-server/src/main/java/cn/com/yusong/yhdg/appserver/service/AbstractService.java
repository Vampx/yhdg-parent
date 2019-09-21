package cn.com.yusong.yhdg.appserver.service;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {
    static final Logger log = LogManager.getLogger(AbstractService.class);

    @Autowired
    protected AppConfig config;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    OrderIdMapper orderIdMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    DictItemMapper dictItemMapper;
    @Autowired
    CustomerCouponTicketGiftMapper customerCouponTicketGiftMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    AreaMapper areaMapper;
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    protected AlipayfwClientHolder alipayfwClientHolder;
    @Autowired
    protected WxPayServiceHolder wxPayServiceHolder;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    public AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
    @Autowired
    public AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;
    @Autowired
    public AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    LaxinSettingMapper laxinSettingMapper;
    @Autowired
    DeductionTicketOrderMapper deductionTicketOrderMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;

    protected String staticPath(String imagePath) {
        if (StringUtils.isEmpty(imagePath)) {
            return imagePath;
        } else {
            return config.staticUrl + imagePath;
        }
    }

    protected SystemBatteryType findBatteryType(int batteryType) {
        String key = CacheKey.key(CacheKey.K_BATTERY_TYPE_V_TYPE_NAME, batteryType);
        SystemBatteryType value = (SystemBatteryType) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = systemBatteryTypeMapper.find(batteryType);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
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

    public String findAgentConfigValue(String id, int agentId) {
        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_CONFIG_VALUE, id, agentId);
        String value = (String) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = agentSystemConfigMapper.findConfigValue(id, agentId);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
    }

    public RestResult updateCustomerBalance(Customer customer, int money, List<Integer> balanceList) {
        int agentBalance = 0, balance = 0, giftBalance = 0;
        if ((customer.getBalance() + customer.getGiftBalance()) < money) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
        if (customer.getBalance() >= money) {
            balance = money;
        } else {
            balance = customer.getBalance();
            giftBalance = money - balance;
        }

        balanceList.add(balance);
        balanceList.add(giftBalance);
        int effect = customerMapper.updateBalance(customer.getId(), -balance, -giftBalance);
        if (effect > 0) {
            return RestResult.SUCCESS;
        } else {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
    }

    public Area findArea(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_AREA, id);
        Area area = (Area) memCachedClient.get(key);
        if (area != null) {
            return area;
        }
        area = areaMapper.find(id);

        memCachedClient.set(key, area, MemCachedConfig.CACHE_ONE_WEEK);
        return area;
    }

    public Map payByAlipay(int partnerId, String orderId, Integer money, long customerId, Integer sourceType, String subject, String body) {
        return payByAlipay(partnerId, orderId, money, customerId, sourceType, subject, body, null);
    }

    public Map payByAlipay(int partnerId, String orderId, Integer money, long customerId, Integer sourceType, String subject, String body, String memo) {
        AlipayPayOrder alipayPayOrder = new AlipayPayOrder();
        alipayPayOrder.setId(newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER));
        alipayPayOrder.setPartnerId(partnerId);
        alipayPayOrder.setCustomerId(customerId);
        alipayPayOrder.setMoney(money);
        alipayPayOrder.setSourceType(sourceType);
        alipayPayOrder.setSourceId(orderId);
        alipayPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        alipayPayOrder.setMemo(memo);
        alipayPayOrder.setCreateTime(new Date());
        alipayPayOrderMapper.insert(alipayPayOrder);

        Partner partner = partnerMapper.find(partnerId);

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", partner.getAlipayAppId(), partner.getAlipayPriKey(), "json", Constant.ENCODING_UTF_8, partner.getAlipayAliKey(), "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(alipayPayOrder.getId());
        model.setTimeoutExpress("30m");
        model.setEnablePayChannels("balance,moneyFund,pcredit,pcreditpayInstallment,creditCard,credit_group,debitCardExpress,mcard,pcard,bankPay");
        model.setTotalAmount(String.format("%.2f", 1.0 * money / 100));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(config.getStaticUrl() + Constant.ALIPAY_PAY_OK);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            Map map = new HashMap();
            map.put("alipayParam", response.getBody());
            return map;
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Map payByAlipayfw(int partnerId, Integer agentId, String orderId, Integer money, long customerId, Integer sourceType, String subject, String body) {
        return payByAlipayfw(partnerId, agentId, orderId, money, customerId, sourceType, subject, body, null);
    }

    public Map payByAlipayfw(int partnerId, Integer agentId, String orderId, Integer money, long customerId, Integer sourceType, String subject, String body, String memo) {
        //实例化客户端
        AlipayClient alipayClient = alipayfwClientHolder.getPartner(partnerId);
        if(alipayClient == null) {
            throw new IllegalArgumentException(String.format("AlipayClient is null(partnerId=%d)", partnerId));
        }
        Customer customer = customerMapper.find(customerId);

        AlipayfwPayOrder alipayfwPayOrder = new AlipayfwPayOrder();
        alipayfwPayOrder.setId(newOrderId(OrderId.OrderIdType.ALIPAYFW_PAY_ORDER));
        alipayfwPayOrder.setPartnerId(partnerId);
        alipayfwPayOrder.setAgentId(agentId);
        alipayfwPayOrder.setCustomerId(customerId);
        alipayfwPayOrder.setMobile(customer.getMobile());
        alipayfwPayOrder.setCustomerName(customer.getFullname());
        alipayfwPayOrder.setMoney(money);
        alipayfwPayOrder.setSourceType(sourceType);
        alipayfwPayOrder.setSourceId(orderId);
        alipayfwPayOrder.setOrderStatus(AlipayfwPayOrder.Status.INIT.getValue());
        alipayfwPayOrder.setMemo(memo);
        alipayfwPayOrder.setCreateTime(new Date());
        alipayfwPayOrderMapper.insert(alipayfwPayOrder);

        // 从支付宝生活号改成支付宝当面付
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(alipayfwPayOrder.getId());
        model.setTotalAmount(String.format("%.2f", 1.0 * money / 100));
        model.setSubject(subject);
        model.setBody(body);
        model.setBuyerId(customer.getFwOpenId());
        ExtendParams params = new ExtendParams();
        params.setSysServiceProviderId(Constant.ALIPAY_SYS_SERVICE_PROVIDER_ID);
        model.setExtendParams(params);
        model.setTimeoutExpress("30m");

        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(config.getStaticUrl() + Constant.ALIPAYFW_PAY_OK);
        request.setBizModel(model);

        try {
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            Map map = new HashMap();
            map.put("tradeNo", response.getTradeNo());
            if (log.isDebugEnabled()) {
                log.debug("payByAlipayfw: {}", AppUtils.encodeJson(response));
            }

            return map;
        } catch (IOException e) {
            log.error("payByAlipayfw 支付错误", e);
            throw new RuntimeException(e);
        } catch (AlipayApiException e) {
            log.error("payByAlipayfw 错误", e);
            throw new RuntimeException(e);
        }

//        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//        model.setBody(body);
//        model.setSubject(subject);
//        model.setOutTradeNo(alipayfwPayOrder.getId());
//        model.setTimeoutExpress("30m");
//        model.setEnablePayChannels("balance,moneyFund,pcredit,pcreditpayInstallment,creditCard,credit_group,debitCardExpress,mcard,pcard,bankPay");
//        model.setTotalAmount(String.format("%.2f", 1.0 * money / 100));
//        model.setProductCode("QUICK_MSECURITY_PAY");
//        request.setBizModel(model);
//        request.setNotifyUrl(config.getStaticUrl() + Constant.ALIPAYFW_PAY_OK);
//        try {
//            //这里和普通的接口调用不同，使用的是sdkExecute
//            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            Map map = new HashMap();
//            map.put("alipayParam", response.getBody());
//            return map;
//        } catch (AlipayApiException e) {
//            throw new RuntimeException(e);
//        }
    }

    public String newOrderId(OrderId.OrderIdType type) {
        Calendar calendar = new GregorianCalendar();
        int suffix = calendar.get(Calendar.YEAR);
        OrderId orderId = new OrderId(type, suffix);
        orderIdMapper.insert(orderId);
        long id = orderId.getId();

        return newOrderId(id, calendar, type);
    }

    public CabinetBox findOneEmptyBoxNum(String cabinetId, int batteryType) {
        List<Integer> typeList = new ArrayList<Integer>();
        typeList.add(CabinetBox.TYPE_NOT_SUPPORT_CHARGE);
        typeList.add(batteryType);

        return cabinetBoxMapper.findOneEmptyBoxNum(cabinetId, CabinetBox.BoxStatus.EMPTY.getValue(), ConstEnum.Flag.TRUE.getValue(), ConstEnum.Flag.TRUE.getValue(), typeList);
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

    /**
     * 赠送优惠券
     */

    public void giveCouponTicket(String sourceId, Integer category, Integer sourceType, Integer type, Integer dayCount, int agentId, int ticketType, String customerMobile) {

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
            cct.setSourceId(sourceId);
            cct.setSourceType(sourceType);
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

    /**
     * 计算套餐费用
     */
    public void dealPacketPeriodPrice(Date beginTime, Map packetPeriodMap) {
        int monthType = 0;
        if(monthType == 0){
            //兼容后台设置天数错误问题
            int monthCount = Integer.parseInt(packetPeriodMap.get("dayCount").toString()) / 30;
            if(Integer.parseInt(packetPeriodMap.get("dayCount").toString()) % 30 > 0) {
                if (monthCount == 0) {
                    monthCount = 1;
                }
            }
            int price = Integer.parseInt(packetPeriodMap.get("price").toString());

            Calendar c = Calendar.getInstance();
            c.setTime(beginTime);
            int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
            int last = c.getActualMaximum(Calendar.DAY_OF_MONTH);//获取本月天数
            int totalDays = last;
            int remainDays = last - day + 1;//包括今天
            for(int i=0;i<monthCount - 1 ;i++){
                c.add(Calendar.MONTH,1);
                int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                totalDays += days;
                remainDays += days;
            }
            packetPeriodMap.put("beginTime", beginTime);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.setTime(DateUtils.truncate(c.getTime(), Calendar.DATE));
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.SECOND , -1);
            packetPeriodMap.put("endTime", c.getTime());
            packetPeriodMap.put("realDayCount", remainDays);

            //金额处理
            BigDecimal bd = null;
            if(Integer.parseInt(packetPeriodMap.get("dayCount").toString()) % 30 > 0) {
                bd = new BigDecimal(price * 1d / Integer.parseInt(packetPeriodMap.get("dayCount").toString()) * remainDays);
            }else{
                bd = new BigDecimal(price * 1d / totalDays * remainDays);
            }
            int realPrice = bd.intValue();
            if(realPrice  >= 100){
                bd = bd.setScale(-2, BigDecimal.ROUND_DOWN);//丢弃分，精确到元
                realPrice = bd.intValue();
            }else if(realPrice > 0){
                //bd = bd.setScale(0, BigDecimal.ROUND_DOWN);//精确到分
                realPrice = 100;//最低支付1元
            }
            packetPeriodMap.put("realPrice", realPrice);
        } else {
            int dayCount = Integer.parseInt(packetPeriodMap.get("dayCount").toString());
            int price = Integer.parseInt(packetPeriodMap.get("price").toString());

            Date endTime = YhdgUtils.addDay(beginTime, dayCount);
            packetPeriodMap.put("beginTime", beginTime);
            packetPeriodMap.put("endTime", endTime);
            packetPeriodMap.put("realDayCount", dayCount);
            packetPeriodMap.put("realPrice", price);
        }
    }

    //这里不要加事务 由调用方法加事务
    public void handleLaxinCustomerByMonth(Agent agent, Customer customer, int packetPeriodMoney) {
        //客户买好押金后 处理拉新记录
        LaxinCustomer laxinCustomer = laxinCustomerMapper.findByTargetMobile(agent.getPartnerId(), customer.getMobile());
        if (laxinCustomer != null
                && agent.getId().equals(laxinCustomer.getAgentId())
                && laxinCustomer.getForegiftTime() != null
                && laxinCustomer.getIncomeType() != null
                && laxinCustomer.getIncomeType() == Laxin.IncomeType.MONTH.getValue()
                && laxinCustomer.getPacketPeriodMonth() != null
                && laxinCustomer.getPacketPeriodMonth() > 0
                && laxinCustomer.getPacketPeriodMoney() != null
                && laxinCustomer.getPacketPeriodMoney() > 0
                && laxinCustomer.getPacketPeriodExpireTime() != null
                && laxinCustomer.getPacketPeriodExpireTime().getTime() >= System.currentTimeMillis()) {

            LaxinRecord laxinRecord = new LaxinRecord();
            laxinRecord.setId(newOrderId(OrderId.OrderIdType.LAXIN_RECORD));
            laxinRecord.setAgentId(agent.getId());
            laxinRecord.setAgentName(agent.getAgentName());
            laxinRecord.setAgentCode(agent.getAgentCode());
            laxinRecord.setLaxinId(laxinCustomer.getLaxinId());
            laxinRecord.setLaxinMobile(laxinCustomer.getLaxinMobile());
            laxinRecord.setLaxinMoney(laxinCustomer.getPacketPeriodMoney());
            laxinRecord.setTargetCustomerId(customer.getId());
            laxinRecord.setTargetMobile(customer.getMobile());
            laxinRecord.setTargetFullname(customer.getFullname());
            laxinRecord.setStatus(LaxinRecord.Status.WAIT.getValue());
            laxinRecord.setIncomeType(laxinCustomer.getIncomeType());
            laxinRecord.setForegiftMoney(0);
            laxinRecord.setPacketPeriodMoney(packetPeriodMoney);
            laxinRecord.setCreateTime(new Date());
            laxinRecordMapper.insert(laxinRecord);
        }
    }


    /**
     * 运营商押金池
     * @param customerForegiftOrder
     */
    public void handleAgentForegift(CustomerForegiftOrder customerForegiftOrder) {
        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(customerForegiftOrder.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(),  AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;
        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateForegift(customerForegiftOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney inOutMoney = new AgentForegiftInOutMoney();
            inOutMoney.setAgentId(customerForegiftOrder.getAgentId());
            inOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            inOutMoney.setMoney(customerForegiftOrder.getMoney());
            inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_CUSTOMER_FOREGIFT.getValue());
            inOutMoney.setBizId(customerForegiftOrder.getId());
            inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
            inOutMoney.setBalance(foregiftBalance);
            inOutMoney.setRemainMoney(foregiftRemainMoney);
            inOutMoney.setRatio(foregiftBalanceRatio);
            inOutMoney.setOperator(customerForegiftOrder.getCustomerFullname());
            inOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(inOutMoney);
        }
    }

    //这里不要加事务 由调用方法加事务
    public void handleLaxinCustomer(Agent agent, Customer customer, int foregiftMoney, int packetPeriodMoney, Date foregiftTime, Date exchangeTime) {
        //客户买好押金后 处理拉新记录
        LaxinCustomer laxinCustomer = laxinCustomerMapper.findByTargetMobile(agent.getPartnerId(), customer.getMobile());
        if (laxinCustomer != null && agent.getId().equals(laxinCustomer.getAgentId())) {
            if (laxinCustomer.getForegiftTime() == null && laxinCustomer.getExchangeTime() == null) {
                Laxin laxin = laxinMapper.find(laxinCustomer.getLaxinId());
                if (laxin != null) {
                    Date now = new Date();

                    if (laxin.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
                        laxinCustomerMapper.updateByTimes(laxinCustomer.getId(), laxin.getIncomeType(), laxin.getLaxinMoney(), customer.getId(), customer.getFullname(), foregiftTime, exchangeTime);
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
                            laxinRecord.setPacketPeriodMoney(packetPeriodMoney);
                            laxinRecord.setCreateTime(now);
                            laxinRecordMapper.insert(laxinRecord);
                        }

                    } else if (laxin.getIncomeType() == Laxin.IncomeType.MONTH.getValue()) {
                        Date packetPeriodExpireTime = DateUtils.addMonths(now, laxin.getPacketPeriodMonth());
                        laxinCustomerMapper.updateByMonth(laxinCustomer.getId(), laxin.getIncomeType(), laxin.getPacketPeriodMoney(), laxin.getPacketPeriodMonth(), packetPeriodExpireTime, customer.getId(), customer.getFullname(), foregiftTime, exchangeTime);
                    }

                    Customer laxinCustomerAccount = customerMapper.findByMobile(customer.getPartnerId(), laxin.getMobile());
                    customerMapper.updateLaxinInfo(customer.getId(), laxin.getMobile(), laxinCustomerAccount == null ? "" : laxinCustomerAccount.getFullname());
                }
            }
        }


        //自动成为拉新人员(暂时就百里新能源自动拉新)
        Laxin laxin = laxinMapper.findByAgentMobile(agent.getId(), customer.getMobile());
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


    public void insertDeductionTicketOrder(CustomerForegiftOrder customerForegiftOrder, CustomerCouponTicket deductionTicket){
        if(customerForegiftOrder.getDeductionTicketMoney()>0) {
            DeductionTicketOrder order = new DeductionTicketOrder();
            order.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            order.setAgentId(customerForegiftOrder.getAgentId());
            order.setAgentName(customerForegiftOrder.getAgentName());
            order.setAgentCode(customerForegiftOrder.getAgentCode());
            order.setCustomerId(customerForegiftOrder.getCustomerId());
            order.setFullname(customerForegiftOrder.getCustomerFullname());
            order.setMobile(customerForegiftOrder.getCustomerMobile());
            order.setCreateTime(new Date());
            order.setMoney(customerForegiftOrder.getDeductionTicketMoney());
            order.setTicketMoney(deductionTicket.getMoney());
            deductionTicketOrderMapper.insert(order);
        }
    }

    public CustomerMultiOrder createCustomerMultiOrder(Agent agent, Customer customer, int money, int type){
        CustomerMultiOrder customerMultiOrder = new CustomerMultiOrder();
        customerMultiOrder.setPartnerId(customer.getPartnerId());
        customerMultiOrder.setAgentId(agent.getId());
        customerMultiOrder.setAgentName(agent.getAgentName());
        customerMultiOrder.setAgentCode(agent.getAgentCode());
        customerMultiOrder.setCustomerId(customer.getId());
        customerMultiOrder.setFullname(customer.getFullname());
        customerMultiOrder.setMobile(customer.getMobile());
        customerMultiOrder.setTotalMoney(money);
        customerMultiOrder.setDebtMoney(money);
        customerMultiOrder.setStatus(CustomerMultiOrder.Status.NOT_PAY.getValue());
        customerMultiOrder.setType(type);
        customerMultiOrder.setCreateTime(new Date());
        customerMultiOrderMapper.insert(customerMultiOrder);
        return customerMultiOrder;
    }

    /**
     * 运营商押金池
     * @param rentForegiftOrder
     */
    public void handleZdAgentForegift(RentForegiftOrder rentForegiftOrder) {
        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = rentForegiftOrderMapper.sumMoneyByAgent(rentForegiftOrder.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(rentForegiftOrder.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(rentForegiftOrder.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;
        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateZdForegift(rentForegiftOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney inOutMoney = new AgentForegiftInOutMoney();
            inOutMoney.setAgentId(rentForegiftOrder.getAgentId());
            inOutMoney.setCategory(ConstEnum.Category.RENT.getValue());
            inOutMoney.setMoney(rentForegiftOrder.getMoney());
            inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_CUSTOMER_FOREGIFT.getValue());
            inOutMoney.setBizId(rentForegiftOrder.getId());
            inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
            inOutMoney.setBalance(foregiftBalance);
            inOutMoney.setRemainMoney(foregiftRemainMoney);
            inOutMoney.setRatio(foregiftBalanceRatio);
            inOutMoney.setOperator(rentForegiftOrder.getCustomerFullname());
            inOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(inOutMoney);
        }
    }
}
