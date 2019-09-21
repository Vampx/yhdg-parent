package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.OrderStatusExpireException;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BatteryOrderService extends AbstractService {
    final static Logger log = LogManager.getLogger(BatteryOrderService.class);

    @Autowired
    AppConfig config;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryOrderRefundMapper batteryOrderRefundMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    ExchangePriceTimeMapper exchangePriceTimeMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    WeixinmaPayOrderMapper weixinmaPayOrderMapper;
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentMpOpenIdMapper agentMpOpenIdMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    BatteryOrderBatteryReportLogMapper batteryOrderBatteryReportLogMapper;
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;

    public BatteryOrder find(String id) {
        return batteryOrderMapper.find(id);
    }

    public int findTodayOrderCount(long customerId) {
        return batteryOrderMapper.findCountByCustomer(customerId, OrderId.PREFIX_BATTERY + DateFormatUtils.format(new Date(), OrderId.DATE_FORMAT) + "%");
    }

    public List<BatteryOrder> findListByCustomer(Long customerId, Integer orderStatus, Integer offset, Integer limit) {
        return batteryOrderMapper.findListByCustomer(customerId, orderStatus, offset, limit);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BatteryOrder createNewOrder(Customer customer, Battery battery, Cabinet cabinet, CabinetBox cabinetBox, BespeakOrder bespeakOrder) {
        Date date = new Date();
//        //查询电池是否在库存中
//        ShopStoreBattery shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
//        //去库存
//        if(shopStoreBattery != null){
//            Shop shop = shopMapper.find(shopStoreBattery.getShopId());
//            if(shop != null){
//                shopId = shop.getId();
//                shopName = shop.getShopName();
//                shopStoreBatteryMapper.clearBattery(shopStoreBattery.getShopId(), battery.getId());
//            }
//        }


        BatteryOrder order = new BatteryOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
        order.setPartnerId(customer.getPartnerId());
        order.setBatteryType(battery.getType());

        order.setCustomerId(customer.getId());
        Customer dbCustomer = customerMapper.find(customer.getId());
        if (dbCustomer != null) {
            order.setTakeAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                order.setTakeAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        order.setCustomerMobile(customer.getMobile());
        order.setCustomerFullname(customer.getFullname());
        if(cabinet != null){
            order.setAgentId(cabinet.getAgentId());
            order.setTakeCabinetId(cabinet.getId());
            order.setTakeCabinetName(cabinet.getCabinetName());
            order.setTakeBoxNum(cabinetBox.getBoxNum());
            order.setProvinceId(cabinet.getProvinceId());
            order.setCityId(cabinet.getCityId());
            order.setDistrictId(cabinet.getDistrictId());
            order.setAddress(cabinet.getAddress());
            order.setOrderStatus(BatteryOrder.OrderStatus.INIT.getValue());
        }else{
            order.setAgentId(customer.getAgentId());
            order.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        }
        order.setTakeTime(new Date());
        order.setBatteryId(battery.getId());
        order.setInitVolume(battery.getVolume());
        order.setCurrentVolume(battery.getVolume());
        order.setInitCapacity(battery.getCurrentCapacity());
        order.setCurrentCapacity(battery.getCurrentCapacity());
        order.setOpenTime(date);
        order.setCreateTime(date);
        order.setCurrentDistance(0);
        order.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        batteryOrderMapper.insert(order);

        if(cabinet != null){
            //查询用户是否有预约订单
            if(bespeakOrder != null){
                //解锁
                Integer status = null;
                if(bespeakOrder.getBespeakCabinetId().equals(cabinet.getId())
                        && bespeakOrder.getBespeakBoxNum().equals(cabinetBox.getBoxNum())){
                    cabinetBoxMapper.unlockBox(bespeakOrder.getBespeakCabinetId(),bespeakOrder.getBespeakBoxNum(), CabinetBox.BoxStatus.BESPEAK.getValue(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue());
                    status = BespeakOrder.Status.TAKE.getValue();
                }else{
                    cabinetBoxMapper.unlockBox(bespeakOrder.getBespeakCabinetId(),bespeakOrder.getBespeakBoxNum(), CabinetBox.BoxStatus.BESPEAK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
                    status = BespeakOrder.Status.OTHER_TAKE.getValue();
                }
                //更新预约单
                bespeakOrder.setTakeCabinetId(cabinet.getId());
                bespeakOrder.setTakeCabinetName(cabinet.getCabinetName());
                bespeakOrder.setTakeBoxNum(cabinetBox.getBoxNum());
                bespeakOrder.setTakeBatteryId(battery.getId());
                bespeakOrder.setTakeTime(new Date());
                bespeakOrder.setStatus(status);
                bespeakOrderMapper.take(bespeakOrder);
            }

            cabinetBoxMapper.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue());
            batteryMapper.updateCustomerUse(battery.getId(), Battery.Status.IN_BOX_CUSTOMER_USE.getValue(), order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());
        }else{
            batteryMapper.updateCustomerUse(battery.getId(), Battery.Status.CUSTOMER_OUT.getValue(), order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());
        }

        //写入用户关联用户信息
        CustomerExchangeBattery customerExchangeBattery = new CustomerExchangeBattery();
        customerExchangeBattery.setCustomerId(customer.getId());
        customerExchangeBattery.setAgentId(order.getAgentId());
        customerExchangeBattery.setBatteryId(battery.getId());
        customerExchangeBattery.setBatteryType(battery.getType());
        customerExchangeBattery.setBatteryOrderId(order.getId());
        customerExchangeBattery.setCreateTime(new Date());
        customerExchangeBatteryMapper.insert(customerExchangeBattery);

        //取新电时用户套餐变为使用中
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customer.getId(), PacketPeriodOrder.Status.USED.getValue(), order.getAgentId(), order.getBatteryType());
        if (packetPeriodOrder == null) {
            packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customer.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), order.getAgentId(), order.getBatteryType());
        }

        Agent agent = agentMapper.find(order.getAgentId());
        //客户首次换电处理拉新记录
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customer.getId());
        if (customerExchangeInfo != null) {
            CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerExchangeInfo.getForegiftOrderId());
            if (customerForegiftOrder != null && packetPeriodOrder != null) {
                //抵扣金额为空 非VIP套餐客户
                if (customerForegiftOrder.getReduceMoney() == null) {
                    handleLaxinCustomer(agent, customer, customerForegiftOrder.getMoney(), packetPeriodOrder.getMoney(), customerForegiftOrder.getCreateTime(), order.getCreateTime());
                }
            }
        }

        if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.NOT_USE.getValue()) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, packetPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH),-1);
            int eft = packetPeriodOrderMapper.updateStatus(packetPeriodOrder.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.USED.getValue(), beginTime, endTime);
            if (eft > 0) {
                handleLaxinCustomerByMonth(agent, customer, packetPeriodOrder.getMoney());
            }
        }

        if(cabinet != null){
            CabinetOperateLog operateLog = new CabinetOperateLog();
            operateLog.setAgentId(cabinet.getAgentId());

            operateLog.setCabinetId(cabinet.getId());
            operateLog.setCabinetName(cabinet.getCabinetName());
            operateLog.setBoxNum(cabinetBox.getBoxNum());
            operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
            operateLog.setOperatorType(CabinetOperateLog.OperatorType.CUSTOMER.getValue());
            operateLog.setContent(String.format("换电订单%s, 打开满箱成功", order.getId()));
            operateLog.setOperator(customer.getFullname());
            operateLog.setCreateTime(new Date());
            cabinetOperateLogMapper.insert(operateLog);

            PushMetaData metaData = new PushMetaData();
            metaData.setSourceType(PushMessage.SourceType.CUSTOMER_OPEN_NEW_BATTER_BOX.getValue());
            metaData.setSourceId(order.getId());
            metaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(metaData);
        }

        if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
            PushOrderMessage pushOrderMessage = new PushOrderMessage();
            pushOrderMessage.setAgentId(agent.getId());
            pushOrderMessage.setSourceType(PushOrderMessage.SourceType.TAKE.getValue());
            pushOrderMessage.setSourceId(order.getId());
            pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
            pushOrderMessage.setCreateTime(new Date());
            pushOrderMessageMapper.insert(pushOrderMessage);
        }

        return order;
    }

    public List<BatteryOrder> findByPacketOrderId(String packetOrderId, long customerId, int offset, int limit) {
        return batteryOrderMapper.findByPacketOrderId(packetOrderId, customerId, offset, limit);
    }

    public RestResult refund(BatteryOrder batteryOrder, Customer customer) {
        BatteryOrderRefund batteryOrderRefund = new BatteryOrderRefund();
        batteryOrderRefund.setId(batteryOrder.getId());
        batteryOrderRefund.setAgentId(batteryOrder.getAgentId());
        if (batteryOrder.getTakeCabinetId() != null) {
            batteryOrderRefund.setCabinetId(batteryOrder.getTakeCabinetId());
        }
        batteryOrderRefund.setMoney(batteryOrder.getMoney());
        batteryOrderRefund.setRefundTime(new Date());
        batteryOrderRefund.setRefundStatus(BatteryOrderRefund.RefundStatus.REFUND_SUCCESS.getValue());
        batteryOrderRefund.setRefundReason(batteryOrder.getRefundReason());
        batteryOrderRefund.setRefundMoney(batteryOrder.getRefundMoney());
        batteryOrderRefund.setRefundOperator(customer.getFullname());
        batteryOrderRefund.setApplyRefundTime(new Date());
        batteryOrderRefund.setCreateTime(new Date());
        batteryOrderRefund.setCustomerId(customer.getId());
        batteryOrderRefund.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        batteryOrderRefund.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        batteryOrderRefundMapper.insert(batteryOrderRefund);

        if (customerMapper.updateBalance(batteryOrder.getCustomerId(), 0, batteryOrder.getRefundMoney()) == 0) {
            Customer customer1 = customerMapper.find(batteryOrder.getCustomerId());

            CustomerInOutMoney money = new CustomerInOutMoney();
            money.setCustomerId(batteryOrder.getCustomerId());
            money.setType(CustomerInOutMoney.Type.IN.getValue());
            money.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_EXCHANGE_REFUND.getValue());
            money.setBizId(batteryOrder.getId());
            money.setMoney(batteryOrder.getRefundMoney());
            money.setBalance(customer1.getBalance() + customer1.getGiftBalance());
            money.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(money);

            return RestResult.dataResult(RespCode.CODE_1.getValue(), "退款失败", null);
        }
        batteryOrderMapper.updateStatus(batteryOrder.getId(),
                BatteryOrderRefund.RefundStatus.REFUND_SUCCESS.getValue(),
                batteryOrder.getRefundMoney(), new Date(),
                batteryOrder.getRefundReason());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "退款到余额成功", null);
    }

    public int updateMoney(String id, int payType, int price, int money, Integer ticketMoney, String ticketName, Long couponTicketId) {
        return batteryOrderMapper.updateMoney(id, payType, price, money, ticketMoney, ticketName, couponTicketId);
    }

    public RestResult payByAlipay(boolean test, String orderId, long customerId, long couponTicketId) {

        Customer customer = customerMapper.find(customerId);

        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        int price = batteryOrder.getPrice(); //价格
        int money = price; //实付金额
        int couponTicketMoney = 0;
        String couponTicketName = null;

        if (couponTicketId != 0) {
            CustomerCouponTicket couponTicket = customerCouponTicketMapper.find(couponTicketId);

            if (couponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (!couponTicket.getCustomerMobile().equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }

            if (price <= couponTicket.getMoney()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券金额大于支付金额, 请用余额支付");
            }
            money = price - couponTicket.getMoney();
            couponTicketMoney = couponTicket.getMoney();
            couponTicketName = couponTicket.getTicketName();
        }

        if (test) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, Collections.EMPTY_MAP);
        } else {


            //这里先不更新优惠券的状态 等到支付回调再去做更新
            batteryOrderMapper.updateMoney(orderId, ConstEnum.PayType.ALIPAY.getValue(),
                    price, money, couponTicketMoney, couponTicketName,
                    couponTicketId == 0 ? null : couponTicketId);

            Map map = super.payByAlipay(customer.getPartnerId(), batteryOrder.getId(), money, batteryOrder.getCustomerId(), AlipayPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue(), "换电订单支付", "换电订单支付");

            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }
    }

    public RestResult payByAlipayfw(String orderId, long customerId, long couponTicketId) {

        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        int price = batteryOrder.getPrice(); //价格
        int money = price; //实付金额
        int couponTicketMoney = 0;
        String couponTicketName = null;

        Customer customer = customerMapper.find(customerId);

        if (couponTicketId != 0) {
            CustomerCouponTicket couponTicket = customerCouponTicketMapper.find(couponTicketId);

            if (couponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (!couponTicket.getCustomerMobile().equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }

            if (price <= couponTicket.getMoney()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券金额大于支付金额, 请用余额支付");
            }
            money = price - couponTicket.getMoney();
            couponTicketMoney = couponTicket.getMoney();
            couponTicketName = couponTicket.getTicketName();
        }

        //这里先不更新优惠券的状态 等到支付回调再去做更新
        batteryOrderMapper.updateMoney(orderId, ConstEnum.PayType.ALIPAY_FW.getValue(),
                price, money, couponTicketMoney, couponTicketName,
                couponTicketId == 0 ? null : couponTicketId);

        Map map = super.payByAlipayfw(customer.getPartnerId(), batteryOrder.getAgentId(), batteryOrder.getId(), money, batteryOrder.getCustomerId(), AlipayPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue(), "换电订单支付", "换电订单支付");

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    public RestResult qrcodeByAlipay(boolean test, String orderId, long customerId, long couponTicketId) {
        Customer customer = customerMapper.find(customerId);

        RestResult restResult = newAlipayPayOrder(customer.getPartnerId(), orderId, customerId, couponTicketId);
        if (restResult.getCode() != 0) {
            return restResult;
        }
        AlipayPayOrder alipayPayOrder = (AlipayPayOrder) restResult.getData();

        Partner partner = partnerMapper.find(customer.getPartnerId());

        String appId = partner.getAlipayAppId();
        String alipayPublic = partner.getAlipayAliKey();
        String alipayPrivate = partner.getAlipayPriKey();

        Map map = new HashMap();

        if (!test) {
            try {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayPrivate, "json", "UTF-8", alipayPublic, "RSA2"); //获得初始化的AlipayClient
                AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();//创建API对应的request类
                alipayRequest.setNotifyUrl(config.getStaticUrl() + Constant.ALIPAY_PAY_OK);
                alipayRequest.setBizContent("{" +
                        "    \"out_trade_no\":\"" + alipayPayOrder.getId() + "\"," +
                        "    \"total_amount\":\"" + alipayPayOrder.getMoney() + "\"," +
                        "    \"subject\":\"换电订单支付\"," +
                        "    \"timeout_express\":\"5m\"}");//设置业务参数
                AlipayTradePrecreateResponse alipayResponse = alipayClient.execute(alipayRequest);
                if (alipayResponse.isSuccess()) {
                    map.put("qrcode", alipayResponse.getQrCode());
                } else {
                    return RestResult.result(RespCode.CODE_1.getValue(), "支付宝二维码支付调用失败");
                }

            } catch (AlipayApiException e) {
                return RestResult.result(RespCode.CODE_1.getValue(), "支付宝二维码支付调用失败");
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    public RestResult newAlipayPayOrder(int partnerId, String orderId, long customerId, long couponTicketId) {

        Customer customer = customerMapper.find(customerId);

        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        int price = batteryOrder.getPrice(); //价格
        int money = price; //实付金额
        int couponTicketMoney = 0;
        String couponTicketName = null;

        if (couponTicketId != 0) {
            CustomerCouponTicket couponTicket = customerCouponTicketMapper.find(couponTicketId);

            if (couponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (!couponTicket.getCustomerMobile().equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }

            if (price <= couponTicket.getMoney()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券金额大于支付金额, 请用余额支付");
            }
            money = price - couponTicket.getMoney();
            couponTicketMoney = couponTicket.getMoney();
            couponTicketName = couponTicket.getTicketName();
        }

        //这里先不更新优惠券的状态 等到支付回调再去做更新
        batteryOrderMapper.updateMoney(orderId, ConstEnum.PayType.ALIPAY.getValue(),
                price, money, couponTicketMoney, couponTicketName,
                couponTicketId == 0 ? null : couponTicketId);

        AlipayPayOrder alipayPayOrder = new AlipayPayOrder();
        alipayPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER));
        alipayPayOrder.setPartnerId(partnerId);
        alipayPayOrder.setAgentId(batteryOrder.getAgentId());
        alipayPayOrder.setCustomerId(batteryOrder.getCustomerId());
        alipayPayOrder.setMoney(money);
        alipayPayOrder.setSourceType(AlipayPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        alipayPayOrder.setSourceId(batteryOrder.getId());
        alipayPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        alipayPayOrder.setCreateTime(new Date());
        alipayPayOrderMapper.insert(alipayPayOrder);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, alipayPayOrder);
    }

    public RestResult payByWeixin(String orderId, long customerId, long couponTicketId) {
        Customer customer = customerMapper.find(customerId);

        RestResult restResult = newWeixinPayOrder(customer.getPartnerId(), orderId, customerId, couponTicketId);
        if (restResult.getCode() != 0) {
            return restResult;
        }
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("换电订单支付");
        orderRequest.setOutTradeNo(weixinPayOrder.getId());
        orderRequest.setTotalFee( weixinPayOrder.getMoney());//元转成分
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setNotifyUrl(config.getStaticUrl() + Constant.WEIXIN_PAY_OK);
        orderRequest.setTradeType(WxPayConstants.TradeType.APP);


        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.get(weixinPayOrder.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", weixinPayOrder.getPartnerId()));
            }
            log.debug("getWxMpConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxMpPrepayIdResult: {}", string);
            }

            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                long now = System.currentTimeMillis();
                String timeStamp = String.format("%d", now / 1000);
                String nonceStr = String.format("%d", now);

                Map<String, String> stringMap = new HashMap<String, String>();
                stringMap.put("appid", wxPayService.getConfig().getAppId());
                stringMap.put("noncestr", nonceStr);
                stringMap.put("package", "Sign=WXPay");
                stringMap.put("prepayid", result.getPrepayId());
                stringMap.put("partnerid", wxPayService.getConfig().getMchId());
                stringMap.put("timestamp", timeStamp);
                String sign = AppUtils.sign(stringMap, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();
                stringMap.put("sign", sign);

                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, stringMap);

            } else {
                log.warn("return_code error: {}", result.toString());
                return RestResult.result(RespCode.CODE_1.getValue(), "统一下单失败");
            }

        } catch (Exception e) {
            log.error("统一下单失败", e);
            return RestResult.result(RespCode.CODE_1.getValue(), "统一下单失败");
        }
    }

    public RestResult payByWeixinMp(String orderId, long customerId, long couponTicketId) {
        Customer customer = customerMapper.find(customerId);

        RestResult restResult = newWeixinmpPayOrder(customer.getPartnerId(), orderId, customerId, couponTicketId);
        if (restResult.getCode() != 0) {
            return restResult;
        }
        WeixinmpPayOrder weixinmpPayOrder = (WeixinmpPayOrder) restResult.getData();


        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("换电订单支付");
        orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
        orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(customer.getMpOpenId());
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);


        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMp(weixinmpPayOrder.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", weixinmpPayOrder.getPartnerId()));
            }
            log.debug("getWxMpConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxMpPrepayIdResult: {}", string);
            }

            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                long now = System.currentTimeMillis();
                String timeStamp = String.format("%d", now / 1000);
                String nonceStr = String.format("%d", now);
                String signType = "MD5";

                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("appId", wxPayService.getConfig().getAppId());
                map1.put("timeStamp", timeStamp);
                map1.put("nonceStr", nonceStr);
                map1.put("package", "prepay_id=" + result.getPrepayId());
                map1.put("signType", signType);
                String paySign = AppUtils.sign(map1, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();

                Map data = new HashMap<String, String>();
                data.put("appId", wxPayService.getConfig().getAppId());
                data.put("payAppId", customer.getPartnerId());
                data.put("prepayId", result.getPrepayId());
                data.put("package", "prepay_id=" + result.getPrepayId());
                data.put("timeStamp", timeStamp);
                data.put("nonceStr", nonceStr);
                data.put("signType", signType);
                data.put("paySign", paySign);

                if (log.isDebugEnabled()) {
                    log.debug("data: {}", data);
                }
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
            }
        } catch (WxPayException e) {
            log.error("unifiedOrder 统一下单失败, {}", weixinmpPayOrder.getId());
            log.error("unifiedOrder error", e);
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
    }

    public RestResult payByWeixinMa(String orderId, long customerId, long couponTicketId) {
        Customer customer = customerMapper.find(customerId);

        RestResult restResult = newWeixinmaPayOrder(customer.getPartnerId(), orderId, customerId, couponTicketId);
        if (restResult.getCode() != 0) {
            return restResult;
        }
        WeixinmaPayOrder weixinmaPayOrder = (WeixinmaPayOrder) restResult.getData();


        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("换电订单支付");
        orderRequest.setOutTradeNo(weixinmaPayOrder.getId());
        orderRequest.setTotalFee( weixinmaPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(customer.getMaOpenId());
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);


        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMa(weixinmaPayOrder.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMaService is null (partnerId=%d)", weixinmaPayOrder.getPartnerId()));
            }
            log.debug("getWxMaConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxMaPrepayIdResult: {}", string);
            }

            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                long now = System.currentTimeMillis();
                String timeStamp = String.format("%d", now / 1000);
                String nonceStr = String.format("%d", now);
                String signType = "MD5";

                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("appId", wxPayService.getConfig().getAppId());
                map1.put("timeStamp", timeStamp);
                map1.put("nonceStr", nonceStr);
                map1.put("package", "prepay_id=" + result.getPrepayId());
                map1.put("signType", signType);
                String paySign = AppUtils.sign(map1, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();

                Map data = new HashMap<String, String>();
                data.put("appId", wxPayService.getConfig().getAppId());
                data.put("payAppId", customer.getPartnerId());
                data.put("prepayId", result.getPrepayId());
                data.put("package", "prepay_id=" + result.getPrepayId());
                data.put("timeStamp", timeStamp);
                data.put("nonceStr", nonceStr);
                data.put("signType", signType);
                data.put("paySign", paySign);

                if (log.isDebugEnabled()) {
                    log.debug("data: {}", data);
                }
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
            }
        } catch (WxPayException e) {
            log.error("unifiedOrder 统一下单失败, {}", weixinmaPayOrder.getId());
            log.error("unifiedOrder error", e);
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
    }

    public RestResult qrcodeByWeixin(boolean test, String orderId, long customerId, long couponTicketId) {
        Customer customer = customerMapper.find(customerId);

        RestResult restResult = newWeixinPayOrder(customer.getPartnerId(), orderId, customerId, couponTicketId);
        if (restResult.getCode() != 0) {
            return restResult;
        }
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("换电订单支付");
        orderRequest.setOutTradeNo(weixinPayOrder.getId());
        orderRequest.setTotalFee( weixinPayOrder.getMoney());//元转成分
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setNotifyUrl(config.getStaticUrl() + Constant.WEIXIN_PAY_OK);
        orderRequest.setTradeType(WxPayConstants.TradeType.NATIVE);
        orderRequest.setProductId(weixinPayOrder.getId());

        if (test) {
            return RestResult.SUCCESS;
        }

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.get(weixinPayOrder.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", weixinPayOrder.getPartnerId()));
            }
            log.debug("getWxMpConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxMpPrepayIdResult: {}", string);
            }

            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                Map map = new HashMap();
                map.put("qrcode", result.getCodeURL());
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
            } else {
                return RestResult.result(RespCode.CODE_1.getValue(), "微信统一下单失败");
            }

        } catch (Exception e) {
            return RestResult.result(RespCode.CODE_1.getValue(), "微信统一下单失败");
        }
    }

    public RestResult newWeixinPayOrder(int partnerId, String orderId, long customerId, long couponTicketId) {
        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        Customer customer = customerMapper.find(customerId);

        int price = batteryOrder.getPrice(); //价格
        int money = price; //实付金额
        int couponTicketMoney = 0;
        String couponTicketName = null;

        if (couponTicketId != 0) {
            CustomerCouponTicket couponTicket = customerCouponTicketMapper.find(couponTicketId);

            if (couponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (!couponTicket.getCustomerMobile().equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }

            if (price <= couponTicket.getMoney()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券金额大于支付金额, 请用余额支付");
            }
            money = price - couponTicket.getMoney();
            couponTicketMoney = couponTicket.getMoney();
            couponTicketName = couponTicket.getTicketName();
        }

        //这里先不更新优惠券的状态 等到支付回调再去做更新
        batteryOrderMapper.updateMoney(orderId, ConstEnum.PayType.WEIXIN.getValue(), price, money, couponTicketMoney, couponTicketName, couponTicketId == 0 ? null : couponTicketId);

        WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
        weixinPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setPartnerId(partnerId);
        weixinPayOrder.setAgentId(batteryOrder.getAgentId());
        weixinPayOrder.setMoney(money);
        weixinPayOrder.setSourceId(batteryOrder.getId());
        weixinPayOrder.setSourceType(WeixinPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        weixinPayOrder.setOrderStatus(WeixinPayOrder.Status.INIT.getValue());
        weixinPayOrder.setCreateTime(new Date());
        weixinPayOrder.setCustomerId(batteryOrder.getCustomerId());
        weixinPayOrderMapper.insert(weixinPayOrder);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);
    }

    public RestResult newWeixinmpPayOrder(int partnerId, String orderId, long customerId, long couponTicketId) {

        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        Customer customer = customerMapper.find(customerId);
        int price = batteryOrder.getPrice(); //价格
        int money = price; //实付金额
        int couponTicketMoney = 0;
        String couponTicketName = null;

        if (couponTicketId != 0) {
            CustomerCouponTicket couponTicket = customerCouponTicketMapper.find(couponTicketId);

            if (couponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (!couponTicket.getCustomerMobile().equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }

            if (price <= couponTicket.getMoney()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券金额大于支付金额, 请用余额支付");
            }
            money = price - couponTicket.getMoney();
            couponTicketMoney = couponTicket.getMoney();
            couponTicketName = couponTicket.getTicketName();
        }

        //这里先不更新优惠券的状态 等到支付回调再去做更新
        batteryOrderMapper.updateMoney(orderId, ConstEnum.PayType.WEIXIN_MP.getValue(), price, money, couponTicketMoney, couponTicketName, couponTicketId == 0 ? null : couponTicketId);

        WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
        weixinmpPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
        weixinmpPayOrder.setPartnerId(partnerId);
        weixinmpPayOrder.setAgentId(batteryOrder.getAgentId());
        weixinmpPayOrder.setMoney(money);
        weixinmpPayOrder.setSourceId(batteryOrder.getId());
        weixinmpPayOrder.setSourceType(WeixinPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        weixinmpPayOrder.setOrderStatus(WeixinPayOrder.Status.INIT.getValue());
        weixinmpPayOrder.setCreateTime(new Date());
        weixinmpPayOrder.setCustomerId(batteryOrder.getCustomerId());
        weixinmpPayOrder.setMobile(batteryOrder.getCustomerMobile());
        weixinmpPayOrder.setCustomerName(batteryOrder.getCustomerFullname());
        weixinmpPayOrderMapper.insert(weixinmpPayOrder);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
    }

    public RestResult newWeixinmaPayOrder(int partnerId, String orderId, long customerId, long couponTicketId) {

        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        Customer customer = customerMapper.find(customerId);
        int price = batteryOrder.getPrice(); //价格
        int money = price; //实付金额
        int couponTicketMoney = 0;
        String couponTicketName = null;

        if (couponTicketId != 0) {
            CustomerCouponTicket couponTicket = customerCouponTicketMapper.find(couponTicketId);

            if (couponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (!couponTicket.getCustomerMobile().equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }

            if (price <= couponTicket.getMoney()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券金额大于支付金额, 请用余额支付");
            }
            money = price - couponTicket.getMoney();
            couponTicketMoney = couponTicket.getMoney();
            couponTicketName = couponTicket.getTicketName();
        }

        //这里先不更新优惠券的状态 等到支付回调再去做更新
        batteryOrderMapper.updateMoney(orderId, ConstEnum.PayType.WEIXIN_MA.getValue(), price, money, couponTicketMoney, couponTicketName, couponTicketId == 0 ? null : couponTicketId);

        WeixinmaPayOrder weixinmaPayOrder = new WeixinmaPayOrder();
        weixinmaPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXINMA_PAY_ORDER));
        weixinmaPayOrder.setPartnerId(partnerId);
        weixinmaPayOrder.setAgentId(batteryOrder.getAgentId());
        weixinmaPayOrder.setMoney(money);
        weixinmaPayOrder.setSourceId(batteryOrder.getId());
        weixinmaPayOrder.setSourceType(WeixinPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        weixinmaPayOrder.setOrderStatus(WeixinPayOrder.Status.INIT.getValue());
        weixinmaPayOrder.setCreateTime(new Date());
        weixinmaPayOrder.setCustomerId(batteryOrder.getCustomerId());
        weixinmaPayOrder.setMobile(batteryOrder.getCustomerMobile());
        weixinmaPayOrder.setCustomerName(batteryOrder.getCustomerFullname());
        weixinmaPayOrderMapper.insert(weixinmaPayOrder);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmaPayOrder);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByBalance(String orderId, long customerId, long couponTicketId) {
        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        Customer customer = customerMapper.find(customerId);

        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        if (batteryOrder.getPutCabinetId() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单信息异常,不存在放入站点信息");
        }
        int payType = ConstEnum.PayType.BALANCE.getValue();

        int price = batteryOrder.getPrice(); //价格
        int money = price; //实付金额
        int couponTicketMoney = 0;
        String couponTicketName = null;

        if (couponTicketId != 0) {
            CustomerCouponTicket couponTicket = customerCouponTicketMapper.find(couponTicketId);

            if (couponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (!couponTicket.getCustomerMobile().equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }

            if (couponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }

            if (price <= couponTicket.getMoney()) {
                money = 0;
            } else {
                money = price - couponTicket.getMoney();
            }
            couponTicketMoney = couponTicket.getMoney();
            couponTicketName = couponTicket.getTicketName();
        }
        if (money != 0) {
            List<Integer> balanceList = new ArrayList<Integer>();

            RestResult restResult = updateCustomerBalance(customer, money, balanceList);
            if (restResult.getCode() == RespCode.CODE_0.getValue()) {
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(batteryOrder.getCustomerId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_EXCHANGE_PAY.getValue());
                inOutMoney.setBizId(batteryOrder.getId());
                inOutMoney.setMoney(-money);
                inOutMoney.setBalance(customer.getBalance() - money);
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
            } else {
                return restResult;
            }
        }
        if (couponTicketId != 0) {
            if (customerCouponTicketMapper.useTicket(couponTicketId,
                    new Date(),
                    CustomerCouponTicket.Status.NOT_USER.getValue(),
                    CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new IllegalArgumentException("更新优惠券状态出错");

            }
        }
        //清除客户上的电池引用
        customerExchangeBatteryMapper.clearBattery(customerId, batteryOrder.getBatteryId());

        // 清空格口状态
        CabinetBox cabinetBox = cabinetBoxMapper.find(batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
        if(cabinetBox.getBatteryId() != null && cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.CUSTOMER_USE.getValue()){
            cabinetBoxMapper.unlockBox(batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue(), CabinetBox.BoxStatus.FULL.getValue());
        }

        //TODO 可能是多余的判断 需要测试
//        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(batteryOrder.getPacketPeriodOrderId());
//        if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.USED.getValue()) {
//            payType = ConstEnum.PayType.PACKET.getValue();
//        }

        int effect = batteryOrderMapper.payOk(orderId, BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue(), BatteryOrder.OrderStatus.PAY.getValue(),
                payType, new Date(), price, money,
                couponTicketMoney, couponTicketName, couponTicketId == 0 ? null : couponTicketId);
        if (effect == 0) {
            throw new OrderStatusExpireException();
        }

        if (batteryOrder.getPayTimeoutFaultLogId() != null) {
            faultLogMapper.handle(batteryOrder.getPayTimeoutFaultLogId(),
                    FaultLog.HandleType.SYSTEM.getValue(),
                    null,
                    null,
                    new Date(),
                    FaultLog.Status.WAIT_PROCESS.getValue(),
                    FaultLog.Status.PROCESSED.getValue()
            );
        }

        batteryMapper.clearCustomer(batteryOrder.getBatteryId(), Battery.Status.IN_BOX.getValue());

        //更改柜子格口状态
        cabinetBoxMapper.unlockBox(batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue(), CabinetBox.BoxStatus.FULL.getValue());

        batteryOperateLogMapper.insert(new BatteryOperateLog(batteryOrder.getBatteryId(), BatteryOperateLog.OperateType.CUSTOMER_PAY_OLD.getValue(), customer.getId(), customer.getMobile(), customer.getFullname(), batteryOrder.getPutCabinetId(), batteryOrder.getPutCabinetName(), batteryOrder.getPutBoxNum(), batteryOrder.getCurrentVolume(), new Date()));


        return RestResult.SUCCESS;
    }


    public RestResult getListByCustomer(long customerId, int offset, int limit) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        List<BatteryOrder> orderList = batteryOrderMapper.findListByCustomer(customerId, null, offset, limit);
        List<Map> list = new ArrayList<Map>(orderList.size());

        if (!orderList.isEmpty()) {

            for (BatteryOrder batteryOrder : orderList) {
                NotNullMap line = new NotNullMap(30);
                line.putString("id", batteryOrder.getId());
                line.putString("batteryId", batteryOrder.getBatteryId());
                line.putInteger("batteryType", batteryOrder.getBatteryType());
                line.putString("batteryCode", batteryOrder.getBatteryCode());
                line.putString("shellCode", batteryOrder.getShellCode());
                line.put("batteryTypeName", findBatteryType(batteryOrder.getBatteryType()).getTypeName());

                line.putString("takeCabinetId", batteryOrder.getTakeCabinetId());
                line.putString("takeCabinetName", batteryOrder.getTakeCabinetName());
                line.putString("takeBoxNum", batteryOrder.getTakeBoxNum());
                line.putDateTime("takeTime", batteryOrder.getTakeTime());

                line.putString("putCabinetId", batteryOrder.getPutCabinetId());
                line.putString("putCabinetName", batteryOrder.getPutCabinetName());
                line.putString("putBoxNum", batteryOrder.getPutBoxNum());
                line.putDateTime("putTime", batteryOrder.getPutTime());
                line.putString("orderStatusName", BatteryOrder.OrderStatus.getName(batteryOrder.getOrderStatus()));

                long duration = 0;
                if (batteryOrder.getPutTime() != null && batteryOrder.getTakeTime() != null) {
                    duration = batteryOrder.getPutTime().getTime() - batteryOrder.getTakeTime().getTime();
                } else if (batteryOrder.getPutTime() == null && batteryOrder.getTakeTime() != null) {
                    duration = System.currentTimeMillis() - batteryOrder.getTakeTime().getTime();
                }
                line.putString("duration", AppUtils.formatHhmmss(duration / 1000));

                line.putInteger("distance", batteryOrder.getCurrentDistance());
                line.putInteger("beginVolume", batteryOrder.getInitVolume());
                line.putInteger("endVolume", batteryOrder.getCurrentVolume());


                line.putString("payTypeName", "");
                if (batteryOrder.getPayType() != null) {
                    line.putString("payTypeName", ConstEnum.PayType.getName(batteryOrder.getPayType()));
                }
                line.putString("ticketName", "");
                Integer ticketMoney = batteryOrder.getTicketMoney();
                if (ticketMoney != null) {
                    String ticketName = batteryOrder.getTicketName();
                    if (ticketName == null) {
                        ticketName = "";
                    }
                    line.putString("ticketName", String.format("%.2f", 1d * ticketMoney / 100) + "元" + ticketName);
                }
                list.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", list);
    }

    public RestResult getListByCustomerAndOrderId(long customerId, String orderId, int offset, int limit) {

        List<BatteryOrder> orderList = batteryOrderMapper.findListByCustomerAndOrderId(customerId, orderId, offset, limit);
        List<Map> list = new ArrayList<Map>(orderList.size());

        if (!orderList.isEmpty()) {

            for (BatteryOrder batteryOrder : orderList) {
                NotNullMap line = new NotNullMap(30);
                Battery battery = batteryMapper.find(batteryOrder.getBatteryId());
                line.putString("id", batteryOrder.getId());
                line.putString("batteryId", battery.getCode());
                line.putString("takeCabinetId", batteryOrder.getTakeCabinetId());
                line.putString("takeCabinetName", batteryOrder.getTakeCabinetName());
                line.putString("takeSubcabinetId", batteryOrder.getTakeCabinetId());
                line.putString("takeSubcabinetName", batteryOrder.getTakeCabinetName());
                line.putString("takeBoxNum", batteryOrder.getTakeBoxNum());
                line.putDateTime("takeTime", batteryOrder.getTakeTime());

                line.putInteger("initVolume", batteryOrder.getInitVolume());
                line.putString("putCabinetId", batteryOrder.getPutCabinetId());
                line.putString("putCabinetName", batteryOrder.getPutCabinetName());
                line.putString("putSubcabinetId", batteryOrder.getPutCabinetId());
                line.putString("putSubcabinetName", batteryOrder.getPutCabinetName());
                line.putString("putBoxNum", batteryOrder.getPutBoxNum());
                line.putDateTime("putTime", batteryOrder.getPutTime());
                line.putInteger("volume", batteryOrder.getCurrentVolume());
                line.putString("customerMobile", batteryOrder.getCustomerMobile());

                list.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", list);
    }


    public RestResult findBatteryReportLogByOrderId(String orderId, int offset, int limit) {
        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        String suffix = DateFormatUtils.format(batteryOrder.getCreateTime(), "yyyyww");
        if (StringUtils.isEmpty(batteryOrderBatteryReportLogMapper.findTableExist("hdg_battery_order_battery_report_log_" + suffix))) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池上报轨迹表不存在");
        }

        List<BatteryOrderBatteryReportLog> reportLoglist = batteryOrderBatteryReportLogMapper.findListByOrderId(orderId, suffix, offset, limit);
        List<Map> list = new ArrayList<Map>(reportLoglist.size());
        if (!reportLoglist.isEmpty()) {
            for (BatteryOrderBatteryReportLog batteryOrderBatteryReportLog : reportLoglist) {
                NotNullMap line = new NotNullMap(30);
                line.putString("createTime", DateFormatUtils.format(batteryOrderBatteryReportLog.getReportTime(), "yyyy-MM-dd HH:mm:ss"));
                line.putDouble("lat", batteryOrderBatteryReportLog.getLat());
                line.putDouble("lng", batteryOrderBatteryReportLog.getLng());
                list.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", list);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult complete(String id) {
        BatteryOrder order = batteryOrderMapper.find(id);
        if (order == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        if (order.getOrderStatus() == BatteryOrder.OrderStatus.PAY.getValue() || order.getOrderStatus() == BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单已结束");
        }
        Customer customer = customerMapper.find(order.getCustomerId());

        int effect = batteryOrderMapper.payOk(id, null, BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue(),
                ConstEnum.PayType.PLATFORM.getValue(), new Date(), 0, 0, 0, null, null);
        if (effect == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单状态不对");
        }
        Agent agent = agentMapper.find(order.getAgentId());

        if (effect == 1) {
            customerExchangeBatteryMapper.clearBattery(order.getCustomerId(), order.getBatteryId());
            batteryMapper.clearCustomer(order.getBatteryId(), Battery.Status.NOT_USE.getValue());
            //如果格口状态为客户使用中，修改为已入箱
            if(order.getPutCabinetId() != null && order.getPutBoxNum() != null){
                CabinetBox box = cabinetBoxMapper.find(order.getPutCabinetId(), order.getPutBoxNum());
                if(box.getBatteryId() != null && order.getBatteryId().equals(box.getBatteryId())){
                    // 清空格口状态
                    cabinetBoxMapper.unlockBox(order.getPutCabinetId(), order.getPutBoxNum(), null, CabinetBox.BoxStatus.FULL.getValue());
                }
            }else{
                //电池入库处理
//                if(order.getTakeShopId() != null && order.getOrderStatus() == BatteryOrder.OrderStatus.TAKE_OUT.getValue()){
//                    ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
//                    shopStoreBattery.setCategory(Battery.Category.EXCHANGE.getValue());
//                    shopStoreBattery.setAgentId(order.getAgentId());
//                    shopStoreBattery.setAgentName(agent.getAgentName());
//                    shopStoreBattery.setShopId(order.getTakeShopId());
//                    shopStoreBattery.setShopName(order.getTakeShopName());
//                    shopStoreBattery.setBatteryId(order.getBatteryId());
//                    shopStoreBattery.setCreateTime(new Date());
//                    shopStoreBatteryMapper.insert(shopStoreBattery);
//                }
            }

            if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
                PushOrderMessage pushOrderMessage = new PushOrderMessage();
                pushOrderMessage.setAgentId(agent.getId());
                pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
                pushOrderMessage.setSourceId(order.getId());
                pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                pushOrderMessage.setCreateTime(new Date());
                pushOrderMessageMapper.insert(pushOrderMessage);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", null);
    }

}
