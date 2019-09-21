package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/11/18.
 */
public class MultiPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    MultiPayOrderService multiPayOrderService;

    @Test
    public void foregiftOrderPayOk() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setMoney(100);
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setId("111");
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
        packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue() + ":" + customerForegiftOrder.getId() + "," + OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue() + ":" + packetPeriodOrder.getId());
        insertAlipayPayOrder(order);

        multiPayOrderService.foregiftOrderPayOk(order);

        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrder.getId()));
        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrder.getId()));
        assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", customerCouponTicket.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ? and foregift_order_id = ?", customer.getId(), customerForegiftOrder.getId()));

        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));
    }

    @Test
    public void rentRoregiftOrderPayOk() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setMoney(100);
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setId("111");
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.WAIT_PAY.getValue());
        rentForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        rentPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        insertRentPeriodOrder(rentPeriodOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue() + ":" + rentForegiftOrder.getId() + "," + OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue() + ":" + rentPeriodOrder.getId());
        insertAlipayPayOrder(order);

        multiPayOrderService.rentForegiftOrderPayOk(order);

        assertEquals(RentForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_foregift_order where id = ?", rentForegiftOrder.getId()));
        assertEquals(RentPeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_period_order where id = ?", rentPeriodOrder.getId()));
        assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", customerCouponTicket.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from zd_customer_rent_info where id = ? and foregift_order_id = ?", customer.getId(), rentForegiftOrder.getId()));
    }

    @Test
    public void packetPeriodOrderPayOk() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        String orderId = newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(packetPeriodOrder.getId());
        insertAlipayPayOrder(order);

        multiPayOrderService.packetPeriodOrderPayOk(order);
    }


    @Test
    public void multiPayDetailPayOk() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setMoney(100);
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        customerCouponTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setId("111");
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
        customerForegiftOrder.setMoney(300);
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
        packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        packetPeriodOrder.setMoney(300);
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setTotalMoney(customerForegiftOrder.getMoney()+packetPeriodOrder.getMoney());
        customerMultiOrder.setDebtMoney(customerForegiftOrder.getMoney()+packetPeriodOrder.getMoney());
        insertCustomerMultiOrder(customerMultiOrder);
        Integer num = 0;
        CustomerMultiOrderDetail customerMultiOrderDetail = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                customerForegiftOrder.getId(), CustomerMultiOrderDetail.SourceType.HDGFOREGIFT.getValue(), customerForegiftOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail);
        CustomerMultiOrderDetail customerMultiOrderDetail2 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                packetPeriodOrder.getId(), CustomerMultiOrderDetail.SourceType.HDGPACKETPERIOD.getValue(), packetPeriodOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail2);


        CustomerMultiPayDetail customerMultiPayDetail = newCustomerMultiPayDetail(customerMultiOrder.getId(), ConstEnum.PayType.WEIXIN_MP.getValue(),300);
        insertCustomerMultiPayDetail(customerMultiPayDetail);

        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
        weixinmpPayOrder.setSourceId(customerMultiPayDetail.getId().toString());
        insertWeixinmpPayOrder(weixinmpPayOrder);

        multiPayOrderService.multiPayDetailPayOk(weixinmpPayOrder);
        assertEquals(CustomerMultiOrder.Status.IN_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));

        CustomerMultiPayDetail customerMultiPayDetail2 = newCustomerMultiPayDetail(customerMultiOrder.getId(), ConstEnum.PayType.ALIPAY_FW.getValue(),300);
        insertCustomerMultiPayDetail(customerMultiPayDetail2);

        AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
        alipayfwPayOrder.setSourceId(customerMultiPayDetail2.getId().toString());
        insertAlipayfwPayOrder(alipayfwPayOrder);

        multiPayOrderService.multiPayDetailPayOk(alipayfwPayOrder);
        assertEquals(CustomerMultiOrder.Status.HAVE_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));

        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrder.getId()));
        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrder.getId()));
        assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", customerCouponTicket.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ? and foregift_order_id = ?", customer.getId(), customerForegiftOrder.getId()));

        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));
    }


    @Test
    public void foregiftOrderFirstMoneyPayOk() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setMoney(100);
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setId("111");
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
        packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerInstallmentRecord customerInstallmentRecord = newCustomerInstallmentRecord(agent.getPartnerId(),customer.getId(), agent.getId());
        insertCustomerInstallmentRecord(customerInstallmentRecord);

        CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue(), customerForegiftOrder.getId());
        customerForegiftOrderDetail.setNum(1);
        insertCustomerInstallmentRecordOrderDetail(customerForegiftOrderDetail);

        CustomerInstallmentRecordOrderDetail packetPeriodOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue(), packetPeriodOrder.getId());
        packetPeriodOrderOrderDetail.setNum(2);
        insertCustomerInstallmentRecordOrderDetail(packetPeriodOrderOrderDetail);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insuranceOrder.setStatus(InsuranceOrder.Status.NOT_PAY.getValue());
        insertInsuranceOrder(insuranceOrder);

        CustomerInstallmentRecordOrderDetail insuranceOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.INSURANCE_ORDER.getValue(), insuranceOrder.getId());
        insuranceOrderOrderDetail.setNum(3);
        insertCustomerInstallmentRecordOrderDetail(insuranceOrderOrderDetail);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(customerInstallmentRecord.getId().toString());
        insertAlipayPayOrder(order);

//        multiPayOrderService.foregiftOrderFirstMoneyPayOk(order);

        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrderOrderDetail.getSourceId()));
        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrderDetail.getSourceId()));
        assertEquals(InsuranceOrder.Status.PAID.getValue(), jdbcTemplate.queryForInt("select status from hdg_insurance_order where id = ?", insuranceOrderOrderDetail.getSourceId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ? and foregift_order_id = ?", customer.getId(), customerForegiftOrder.getId()));

        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));
    }

}
