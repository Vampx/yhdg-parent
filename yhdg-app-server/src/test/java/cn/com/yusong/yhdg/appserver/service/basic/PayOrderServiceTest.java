package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/18.
 */
public class PayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    MultiPayOrderService payOrderService;

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

        payOrderService.foregiftOrderPayOk(order);

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

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setId("111");
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.WAIT_PAY.getValue());
        rentForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
        rentForegiftOrder.setShopId(shop.getId());
        rentForegiftOrder.setBatteryId(battery.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        rentPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        insertRentPeriodOrder(rentPeriodOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue() + ":" + rentForegiftOrder.getId() + "," + OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue() + ":" + rentPeriodOrder.getId());
        insertAlipayPayOrder(order);

        payOrderService.rentForegiftOrderPayOk(order);

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

        payOrderService.packetPeriodOrderPayOk(order);

        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id='"+packetPeriodOrder.getId()+"'"));
    }

    @Test
    public void rentPeriodOrderPayOk() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertRentPeriodOrder(rentPeriodOrder);

        String orderId = newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(rentPeriodOrder.getId());
        insertAlipayPayOrder(order);

        payOrderService.rentPeriodOrderPayOk(order);

        assertEquals(RentPeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_period_order where id='"+rentPeriodOrder.getId()+"'"));
    }

    //测试拉新按次
    @Test
    public void handleLaxinCustomer_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setIncomeType(Laxin.IncomeType.TIMES.getValue());
        laxin.setLaxinMoney(100);
        laxin.setMobile("aaaaaaa");
        insertLaxin(laxin);

        Customer laxinCustomerAccount = newCustomer(partner.getId());
        laxinCustomerAccount.setMobile("aaaaaaa");
        insertCustomer(laxinCustomerAccount);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile(customer.getMobile());
        laxinCustomer.setForegiftTime(null);
        insertLaxinCustomer(laxinCustomer);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        laxinSetting.setType(LaxinSetting.Type.REGISTER.getValue());
        insertLaxinSetting(laxinSetting);

        payOrderService.handleLaxinCustomer(agent, customer, 100, 100, new Date(), new Date());

        assertEquals(100, jdbcTemplate.queryForInt("select laxin_money from bas_laxin_record where laxin_id = ? and target_customer_id = ?", laxin.getId(), customer.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_laxin where agent_id = ?", agent.getId()));
    }

    //测试拉新按月
    @Test
    public void handleLaxinCustomer_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setIncomeType(Laxin.IncomeType.MONTH.getValue());
        laxin.setPacketPeriodMoney(100);
        laxin.setPacketPeriodMonth(10);
        insertLaxin(laxin);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile(customer.getMobile());
        laxinCustomer.setForegiftTime(null);
        insertLaxinCustomer(laxinCustomer);

        payOrderService.handleLaxinCustomer(agent, customer, 100, 100, new Date(), new Date());
    }

}
