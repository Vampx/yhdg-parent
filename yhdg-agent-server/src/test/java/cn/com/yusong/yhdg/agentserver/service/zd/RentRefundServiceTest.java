package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.zd.*;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class RentRefundServiceTest extends BaseJunit4Test {

    @Autowired
    RentRefundService rentRefundService;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    RentInsuranceOrderMapper rentInsuranceOrderMapper;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        Page page = rentRefundService.findPage(customer);

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

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertRentPeriodOrder(rentPeriodOrder);

        RentInsuranceOrder rentInsuranceOrder = newRentInsuranceOrder(customer);
        insertRentInsuranceOrder(rentInsuranceOrder);

        Customer object = rentRefundService.find(customer.getId());

        assertNotNull(object);
        assertEquals(1, object.getRentForegiftOrderList().size());
        assertEquals(1, object.getRentPeriodOrderList().size());
        assertEquals(1, object.getRentInsuranceOrderList().size());
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

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
//        rentForegiftOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());//支付类型
        rentForegiftOrder.setMoney(160);
        rentForegiftOrder.setDeductionTicketMoney(100);
        insertRentForegiftOrder(rentForegiftOrder);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setSourceId(rentForegiftOrder.getId());
        weixinPayOrder.setSourceType(PayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue());
        insertWeixinPayOrder(weixinPayOrder);

        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), weixinPayOrder.getSourceId(), weixinPayOrder.getSourceType());
        insertCustomerRefundRecord(customerRefundRecord);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerRentInfo(customerRentInfo);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(rentForegiftOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from zd_customer_rent_info where id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentForegiftOrder.getId()));

        //押金退款
        rentRefundService.refund("aaa", CustomerRefundRecord.RefundType.BALANCE.getValue(), CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue(), rentForegiftOrder.getId(), 200, customerRefundRecord.getId(), null, true);

        //验证运营商押金退款订单是否正确生成
        assertEquals(160+100-200, jdbcTemplate.queryForInt("select remain_money from zd_rent_foregift_refund where customer_id = ?", customer.getId()));
        //验证客户余额
        assertEquals(1000+200, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));
        //验证订单状态
        assertEquals(RentForegiftOrder.Status.REFUND_SUCCESS.getValue(), rentForegiftOrderMapper.find(rentForegiftOrder.getId()).getStatus().intValue());
        //验证客户绑定押金信息是否删除
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from zd_customer_rent_info where id = ?", customer.getId()));
        //验证客户消费轨迹是否添加
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态是否失效
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentForegiftOrder.getId()));
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

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        rentForegiftOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());//支付类型
        rentForegiftOrder.setMoney(160);
        rentForegiftOrder.setDeductionTicketMoney(100);
        insertRentForegiftOrder(rentForegiftOrder);

        AlipayPayOrder alipayPayOrder = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER));
        alipayPayOrder.setSourceId(rentForegiftOrder.getId());
        alipayPayOrder.setSourceType(PayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue());
        insertAlipayPayOrder(alipayPayOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerRentInfo(customerRentInfo);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(rentForegiftOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from zd_customer_rent_info where id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentForegiftOrder.getId()));

        //押金退款
        rentRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue(), rentForegiftOrder.getId(), 200, null, "ssssssss", true);

        //验证运营商押金订单是否正确生成
        assertEquals(160+100-200, jdbcTemplate.queryForInt("select remain_money from zd_rent_foregift_refund where customer_id = ?", customer.getId()));
        //验证客户余额
        assertEquals(1000+(200-160), customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(3, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退支付宝160（1进 1出），抵扣券50（1进）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(RentForegiftOrder.Status.REFUND_SUCCESS.getValue(), rentForegiftOrderMapper.find(rentForegiftOrder.getId()).getStatus().intValue());
        //验证(支付宝)支付订单状态
        assertEquals(PayOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select order_status from bas_alipay_pay_order where id = ?", alipayPayOrder.getId()));
        //验证绑定押金信息是否删除
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from zd_customer_rent_info where id = ?", customer.getId()));
        //验证消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentForegiftOrder.getId()));
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

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        rentForegiftOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());//支付类型
        rentForegiftOrder.setMoney(160);
        rentForegiftOrder.setDeductionTicketMoney(100);
        insertRentForegiftOrder(rentForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerRentInfo(customerRentInfo);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(rentForegiftOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from zd_customer_rent_info where id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentForegiftOrder.getId()));

        //押金退款
        rentRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue(), rentForegiftOrder.getId(), 200, null, "ssssssss", true);

        //验证运营商押金订单是否正确生成
        assertEquals(160+100-200, jdbcTemplate.queryForInt("select remain_money from zd_rent_foregift_refund where customer_id = ?", customer.getId()));
        //验证客户余额
        assertEquals(1000+200, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退余额
        //验证订单状态
        assertEquals(RentForegiftOrder.Status.REFUND_SUCCESS.getValue(), rentForegiftOrderMapper.find(rentForegiftOrder.getId()).getStatus().intValue());
        //验证绑定押金信息是否删除
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from zd_customer_rent_info where id = ?", customer.getId()));
        //验证消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentForegiftOrder.getId()));
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

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
        rentPeriodOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());
        rentPeriodOrder.setMoney(160);
        insertRentPeriodOrder(rentPeriodOrder);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setSourceId(rentPeriodOrder.getId());
        weixinPayOrder.setSourceType(PayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue());
        insertWeixinPayOrder(weixinPayOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(rentPeriodOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentPeriodOrder.getId()));

        //包时段套餐退款
        rentRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue(), rentPeriodOrder.getId(), 100, null, "ssssssss", true);

        //验证客户余额
        assertEquals(1000, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退微信100（1进 1出）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(RentPeriodOrder.Status.REFUND.getValue(), rentPeriodOrderMapper.find(rentPeriodOrder.getId()).getStatus().intValue());
        //验证(微信)支付订单状态
        assertEquals(PayOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select order_status from bas_weixin_pay_order where id = ?", weixinPayOrder.getId()));
        //验证客户消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentPeriodOrder.getId()));
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

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
        rentPeriodOrder.setPayType(ConstEnum.PayType.ALIPAY_FW.getValue());
        rentPeriodOrder.setMoney(160);
        insertRentPeriodOrder(rentPeriodOrder);

        AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
        alipayfwPayOrder.setSourceId(rentPeriodOrder.getId());
        alipayfwPayOrder.setSourceType(PayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue());
        insertAlipayfwPayOrder(alipayfwPayOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setSourceId(rentPeriodOrder.getId());
        customerCouponTicket.setSourceType(OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentPeriodOrder.getId()));

        //包时段套餐退款
        rentRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue(), rentPeriodOrder.getId(), 100, null, "ssssssss", true);

        //验证客户余额
        assertEquals(1000, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退微信100（1进 1出）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(RentPeriodOrder.Status.REFUND.getValue(), rentPeriodOrderMapper.find(rentPeriodOrder.getId()).getStatus().intValue());
        //验证(生活号)支付订单状态
        assertEquals(PayOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select order_status from bas_alipayfw_pay_order where id = ?", alipayfwPayOrder.getId()));
        //验证客户消费轨迹
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_pay_track where customer_id = ?", customer.getId()));
        //验证关联优惠券状态
        assertEquals(CustomerCouponTicket.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where source_id = ?", rentPeriodOrder.getId()));
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

        RentInsuranceOrder rentInsuranceOrder = newRentInsuranceOrder(customer);
        rentInsuranceOrder.setStatus(RentInsuranceOrder.Status.PAID.getValue());
        rentInsuranceOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        rentInsuranceOrder.setMoney(160);
        insertRentInsuranceOrder(rentInsuranceOrder);

        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
        weixinmpPayOrder.setSourceId(rentInsuranceOrder.getId());
        weixinmpPayOrder.setSourceType(PayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        insertWeixinmpPayOrder(weixinmpPayOrder);

        //保险退款
        rentRefundService.refund("aaa", CustomerRefundRecord.RefundType.RETURN.getValue(), CustomerRefundRecord.SourceType.ZDINSURANCE.getValue(), rentInsuranceOrder.getId(), 100, null, "ssssssss", true);

        //验证客户余额
        assertEquals(1000, customerMapper.find(customer.getId()).getBalance().intValue());
        //验证客户流水
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));//退微信100（1进 1出）
        //验证商户流水
        assertEquals(CustomerInOutMoney.Type.OUT.getValue(), jdbcTemplate.queryForInt("select type from bas_partner_in_out_money where partner_id = ?", partner.getId()));//商户出账流水
        //验证源订单状态
        assertEquals(RentInsuranceOrder.Status.REFUND_SUCCESS.getValue(), rentInsuranceOrderMapper.find(rentInsuranceOrder.getId()).getStatus().intValue());
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

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        rentForegiftOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());//支付类型
        rentForegiftOrder.setMoney(160);
        rentForegiftOrder.setDeductionTicketMoney(100);
        insertRentForegiftOrder(rentForegiftOrder);

        Map data = new HashMap();

        rentRefundService.doRefund(CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue(), CustomerRefundRecord.RefundType.RETURN.getValue(),CustomerInOutMoney.BizType.IN_CUSTOMER_FOREGIFT_REFUND.getValue(),
                rentForegiftOrder.getPayType(), rentForegiftOrder.getId(), data, rentForegiftOrder.getMoney(), 200, customer.getId(), partner.getId(), "aa", "reason", true);

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

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        rentForegiftOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());//支付类型
        rentForegiftOrder.setMoney(160);
        rentForegiftOrder.setDeductionTicketMoney(100);
        insertRentForegiftOrder(rentForegiftOrder);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setSourceId(rentForegiftOrder.getId());
        weixinPayOrder.setSourceType(PayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue());
        insertWeixinPayOrder(weixinPayOrder);

        rentRefundService.addPayOrderRefund(weixinPayOrder, rentForegiftOrder.getPayType(), CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue(), rentForegiftOrder.getMoney());

        Map<String, Object> map = jdbcTemplate.queryForMap("select biz_id from bas_weixin_pay_order_refund where order_id = ?", weixinPayOrder.getId());
        assertEquals(rentForegiftOrder.getId(), map.get("biz_id"));
    }
}