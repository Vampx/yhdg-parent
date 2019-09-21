package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRefundServiceTest extends BaseJunit4Test {

    @Autowired
    ExchangeRefundService exchangeRefundService;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        Page page = exchangeRefundService.findPage(customer);

        assertTrue(1 == page.getTotalItems());
        assertTrue(1 == page.getResult().size());
        assertEquals(agent.getAgentName(), ((Customer)page.getResult().get(0)).getAgentName());
    }

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insertInsuranceOrder(insuranceOrder);

        Customer object = exchangeRefundService.find(customer.getId());

        assertNotNull(object);
        assertEquals(1, object.getCustomerForegiftOrderList().size());
        assertEquals(1, object.getPacketPeriodOrderList().size());
        assertEquals(1, object.getInsuranceOrderList().size());
    }

    /**
     * 押金退款，退余额，审核
     */
    @Test
    public void refund_1() throws Exception{
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
//        customerForegiftOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());//支付类型
        customerForegiftOrder.setMoney(160);
        customerForegiftOrder.setDeductionTicketMoney(100);
        insertCustomerForegiftOrder(customerForegiftOrder);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setSourceId(customerForegiftOrder.getId());
        weixinPayOrder.setSourceType(PayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        insertWeixinPayOrder(weixinPayOrder);

        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), weixinPayOrder.getSourceId(), weixinPayOrder.getSourceType());
        insertCustomerRefundRecord(customerRefundRecord);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(customerForegiftOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", customerForegiftOrder.getId()));

        //押金退款
        exchangeRefundService.refund("aaa", CustomerRefundRecord.RefundType.BALANCE.getValue(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue(), customerForegiftOrder.getId(), 200, customerRefundRecord.getId(), null, true);

        //验证运营商押金退款订单是否正确生成
        assertEquals(160+100-200, jdbcTemplate.queryForInt("select remain_money from bas_agent_foregift_refund where customer_id = ?", customer.getId()));
        //验证客户余额
        assertEquals(1000+200, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));
        //验证订单状态
        assertEquals(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), customerForegiftOrderMapper.find(customerForegiftOrder.getId()).getStatus().intValue());
        //验证客户绑定押金信息是否删除
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ?", customer.getId()));
        //验证客户消费轨迹是否添加
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态是否失效
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", customerForegiftOrder.getId()));
        //验证退款记录是否添加
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_refund_record where customer_id = ?", customer.getId()));
    }
    /**
     * 押金退款，原路返回（支付宝），非审核
     */
    @Test
    public void refund_2() throws Exception{
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        customerForegiftOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());//支付类型
        customerForegiftOrder.setMoney(160);
        customerForegiftOrder.setDeductionTicketMoney(100);
        insertCustomerForegiftOrder(customerForegiftOrder);

        AlipayPayOrder alipayPayOrder = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER));
        alipayPayOrder.setSourceId(customerForegiftOrder.getId());
        alipayPayOrder.setSourceType(PayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        insertAlipayPayOrder(alipayPayOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(customerForegiftOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", customerForegiftOrder.getId()));

        //押金退款
        exchangeRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue(), customerForegiftOrder.getId(), 200, null, "ssssssss", true);

        //验证运营商押金订单是否正确生成
        assertEquals(160+100-200, jdbcTemplate.queryForInt("select remain_money from bas_agent_foregift_refund where customer_id = ?", customer.getId()));
        //验证客户余额
        assertEquals(1000+(200-160), customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(3, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退支付宝160（1进 1出），抵扣券50（1进）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), customerForegiftOrderMapper.find(customerForegiftOrder.getId()).getStatus().intValue());
        //验证(支付宝)支付订单状态
        assertEquals(PayOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select order_status from bas_alipay_pay_order where id = ?", alipayPayOrder.getId()));
        //验证绑定押金信息是否删除
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ?", customer.getId()));
        //验证消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", customerForegiftOrder.getId()));
        //验证退款记录
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_refund_record where customer_id = ?", customer.getId()));
    }
    /**
     * 押金退款，原路返回（余额），非审核
     */
    @Test
    public void refund_3() throws Exception{
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        customerForegiftOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());//支付类型
        customerForegiftOrder.setMoney(160);
        customerForegiftOrder.setDeductionTicketMoney(100);
        insertCustomerForegiftOrder(customerForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(customerForegiftOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", customerForegiftOrder.getId()));

        //押金退款
        exchangeRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue(), customerForegiftOrder.getId(), 200, null, "ssssssss", true);

        //验证运营商押金订单是否正确生成
        assertEquals(160+100-200, jdbcTemplate.queryForInt("select remain_money from bas_agent_foregift_refund where customer_id = ?", customer.getId()));
        //验证客户余额
        assertEquals(1000+200, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退余额
        //验证订单状态
        assertEquals(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), customerForegiftOrderMapper.find(customerForegiftOrder.getId()).getStatus().intValue());
        //验证绑定押金信息是否删除
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ?", customer.getId()));
        //验证消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", customerForegiftOrder.getId()));
        //验证退款记录
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_refund_record where customer_id = ?", customer.getId()));
    }
    /**
     * 包时段退款，原路返回（微信），非审核
     */
    @Test
    public void refund_4() throws Exception{
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        packetPeriodOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());
        packetPeriodOrder.setMoney(160);
        insertPacketPeriodOrder(packetPeriodOrder);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setSourceId(packetPeriodOrder.getId());
        weixinPayOrder.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
        insertWeixinPayOrder(weixinPayOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(packetPeriodOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", packetPeriodOrder.getId()));

        //包时段套餐退款
        exchangeRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue(), packetPeriodOrder.getId(), 100, null, "ssssssss", true);

        //验证客户余额
        assertEquals(1000, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退微信100（1进 1出）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(PacketPeriodOrder.Status.REFUND.getValue(), packetPeriodOrderMapper.find(packetPeriodOrder.getId()).getStatus().intValue());
        //验证(微信)支付订单状态
        assertEquals(PayOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select order_status from bas_weixin_pay_order where id = ?", weixinPayOrder.getId()));
        //验证客户消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", packetPeriodOrder.getId()));
        //验证客户退款记录
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_refund_record where customer_id = ?", customer.getId()));
    }
    /**
     * 包时段退款，原路返回（生活号），非审核
     */
    @Test
    public void refund_5() throws Exception{
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        packetPeriodOrder.setPayType(ConstEnum.PayType.ALIPAY_FW.getValue());
        packetPeriodOrder.setMoney(160);
        insertPacketPeriodOrder(packetPeriodOrder);

        AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
        alipayfwPayOrder.setSourceId(packetPeriodOrder.getId());
        alipayfwPayOrder.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
        insertAlipayfwPayOrder(alipayfwPayOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(packetPeriodOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", packetPeriodOrder.getId()));

        //包时段套餐退款
        exchangeRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue(), packetPeriodOrder.getId(), 100, null, "ssssssss", true);

        //验证客户余额
        assertEquals(1000, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退微信100（1进 1出）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(PacketPeriodOrder.Status.REFUND.getValue(), packetPeriodOrderMapper.find(packetPeriodOrder.getId()).getStatus().intValue());
        //验证(生活号)支付订单状态
        assertEquals(PayOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select order_status from bas_alipayfw_pay_order where id = ?", alipayfwPayOrder.getId()));
        //验证客户消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", packetPeriodOrder.getId()));
        //验证客户退款记录
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_refund_record where customer_id = ?", customer.getId()));
    }
    /**
     * 保险退款，原路返回（公众号），非审核
     */
    @Test
    public void refund_6() throws Exception{
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insuranceOrder.setStatus(InsuranceOrder.Status.PAID.getValue());
        insuranceOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        insuranceOrder.setMoney(160);
        insertInsuranceOrder(insuranceOrder);

        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
        weixinmpPayOrder.setSourceId(insuranceOrder.getId());
        weixinmpPayOrder.setSourceType(PayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        insertWeixinmpPayOrder(weixinmpPayOrder);

        //保险退款
        exchangeRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.HDGINSURANCE.getValue(), insuranceOrder.getId(), 100, null, "ssssssss", true);

        //验证客户余额
        assertEquals(1000, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退微信100（1进 1出）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(InsuranceOrder.Status.REFUND_SUCCESS.getValue(), insuranceOrderMapper.find(insuranceOrder.getId()).getStatus().intValue());
        //验证(公众号)支付订单状态
        assertEquals(PayOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select order_status from bas_weixinmp_pay_order where id = ?", weixinmpPayOrder.getId()));
        //验证客户退款记录
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_refund_record where customer_id = ?", customer.getId()));
    }

    @Test
    public void doRefund() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        customerForegiftOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());//支付类型
        customerForegiftOrder.setMoney(160);
        customerForegiftOrder.setDeductionTicketMoney(100);
        insertCustomerForegiftOrder(customerForegiftOrder);

        Map data = new HashMap();

        exchangeRefundService.doRefund(CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue(), CustomerRefundRecord.RefundType.RETURN.getValue(),CustomerInOutMoney.BizType.IN_CUSTOMER_FOREGIFT_REFUND.getValue(),
                customerForegiftOrder.getPayType(), customerForegiftOrder.getId(), data, customerForegiftOrder.getMoney(), 200, customer.getId(), partner.getId(), "aa", "reason", true);

        //验证客户余额
        assertEquals(1160, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退余额
    }

    @Test
    public void addPayOrderRefund() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        customerForegiftOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());//支付类型
        customerForegiftOrder.setMoney(160);
        customerForegiftOrder.setDeductionTicketMoney(100);
        insertCustomerForegiftOrder(customerForegiftOrder);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setSourceId(customerForegiftOrder.getId());
        weixinPayOrder.setSourceType(PayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        insertWeixinPayOrder(weixinPayOrder);

        exchangeRefundService.addPayOrderRefund(weixinPayOrder, customerForegiftOrder.getPayType(), weixinPayOrder.getSourceType(), customerForegiftOrder.getMoney());

        Map<String, Object> map = jdbcTemplate.queryForMap("select biz_id from bas_weixin_pay_order_refund where order_id = ?", weixinPayOrder.getId());
        assertEquals(customerForegiftOrder.getId(), map.get("biz_id"));
    }
}