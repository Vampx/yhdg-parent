package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.apache.commons.lang.time.DateUtils;
import org.bouncycastle.asn1.cms.PasswordRecipientInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/18.
 */
public class PayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    PayOrderService payOrderService;

    @Test
    public void depositOrderPayOk() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder customerDepositOrder = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(customerDepositOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertAlipayPayOrder(order);
        payOrderService.depositOrderPayOk(order);
    }

    @Test
    public void agentForegiftepositOrderPayOk() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        AgentForegiftDepositOrder agentForefiftDepositOrder = newAgentForegiftDepositOrder(partner.getId(), agent.getId());
        agentForefiftDepositOrder.setStatus(AgentForegiftDepositOrder.Status.NOT_PAID.getValue());
        agentForefiftDepositOrder.setOperator("");
        agentForefiftDepositOrder.setMoney(100);
        agentForefiftDepositOrder.setCategory(ConstEnum.Category.EXCHANGE.getValue());

        insertAgentForegiftDepositOrder(agentForefiftDepositOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(agentForefiftDepositOrder.getId());
        order.setSourceType(AlipayPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue());
        insertAlipayPayOrder(order);
        payOrderService.agentForegiftDepositOrderPayOk(order);

        assertEquals(0, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_foregift_in_out_money where agent_id = ?", agent.getId()));
    }

    @Test
    public void agentForegiftepositOrderPayOk_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        AgentForegiftDepositOrder agentForefiftDepositOrder = newAgentForegiftDepositOrder(partner.getId(), agent.getId());
        agentForefiftDepositOrder.setStatus(AgentForegiftDepositOrder.Status.NOT_PAID.getValue());
        agentForefiftDepositOrder.setOperator("");
        agentForefiftDepositOrder.setMoney(100);
        agentForefiftDepositOrder.setCategory(ConstEnum.Category.RENT.getValue());
        insertAgentForegiftDepositOrder(agentForefiftDepositOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(agentForefiftDepositOrder.getId());
        order.setSourceType(AlipayPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue());
        insertAlipayPayOrder(order);
        payOrderService.agentForegiftDepositOrderPayOk(order);

        assertEquals(0, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select zd_foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select zd_foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select zd_foregift_balance_ratio from bas_agent where id = ?", agent.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_foregift_in_out_money where agent_id = ?", agent.getId()));
    }

    //押金赠送
    @Test
    public void foregiftOrderPayOk1() {
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

        //押金赠送
        int type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();
        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setCategory(CustomerCouponTicketGift.Category.EXCHANGE.getValue());
        customerCouponTicketGift.setType(type);
        insertCustomerCouponTicketGift(customerCouponTicketGift);

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

    //租金赠送
    @Test
    public void foregiftOrderPayOk2() {
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
        packetPeriodOrder.setDayCount(10);
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
        packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        //租金赠送
        int type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setCategory(CustomerCouponTicketGift.Category.EXCHANGE.getValue());
        customerCouponTicketGift.setPayCount(10);
        customerCouponTicketGift.setType(type);
        insertCustomerCouponTicketGift(customerCouponTicketGift);

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
    }

    @Test
    public void batteryOrderPayOk() {
        Partner partner = newPartner();
        insertPartner(partner);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);

        CabinetCode cabinetCode = newCabinetCode();
        cabinetCode.setId("10086");
        insertCabinetCode(cabinetCode);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        batteryOrder.setCurrentVolume(100);
        batteryOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        insertBatteryOrder(batteryOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);

        String orderId = newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(batteryOrder.getId());
        insertAlipayPayOrder(order);

        assertTrue(payOrderService.batteryOrderPayOk(order));
    }

    @Test
    public void laxinOrderPayOk() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setStatus(LaxinRecord.Status.WAIT.getValue());
        insertLaxinRecord(laxinRecord);

        LaxinPayOrder laxinPayOrder = newLaxinPayOrder(agent.getId());
        laxinPayOrder.setStatus(LaxinPayOrder.Status.WAIT.getValue());
        insertLaxinPayOrder(laxinPayOrder);

        LaxinPayOrderDetail laxinPayOrderDetail = newLaxinPayOrderDetail(laxinPayOrder.getId(), laxinRecord.getId());
        insertLaxinPayOrderDetail(laxinPayOrderDetail);

        WeixinmpPayOrder payOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), null);
        payOrder.setSourceId(laxinPayOrder.getId());
        insertWeixinmpPayOrder(payOrder);

        payOrderService.laxinOrderPayOk(payOrder);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_laxin_record where order_id = ? and status = ?", laxinPayOrder.getId(), LaxinRecord.Status.TRANSFER.getValue()));
    }

    @Test
    public void agentDepositOrderPayOk() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100);
        insertAgent(agent);

        String orderId = newOrderId(OrderId.OrderIdType.AGENT_DEPOSIT_ORDER);
        AgentDepositOrder agentDepositOrder = newAgentDepositOrder(partner.getId(), agent.getId(), orderId);
        agentDepositOrder.setOperator("aaa");
        agentDepositOrder.setMoney(100);
        insertAgentDepositOrder(agentDepositOrder);

        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), null);
        weixinmpPayOrder.setSourceId(agentDepositOrder.getId());
        payOrderService.agentDepositOrderPayOk(weixinmpPayOrder);

        assertEquals(200, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_in_out_money where agent_id = ?", agent.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_partner_in_out_money where partner_id = ?", partner.getId()));
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

//        payOrderService.foregiftOrderFirstMoneyPayOk(order);

        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrderOrderDetail.getSourceId()));
        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrderDetail.getSourceId()));
        assertEquals(InsuranceOrder.Status.PAID.getValue(), jdbcTemplate.queryForInt("select status from hdg_insurance_order where id = ?", insuranceOrderOrderDetail.getSourceId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ? and foregift_order_id = ?", customer.getId(), customerForegiftOrder.getId()));

        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(customerForegiftOrder.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));
    }


    @Test
    public void foregiftInstallmentMoneyPayOk() {
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
        customerInstallmentRecord.setStatus(CustomerInstallmentRecord.Status.PAY_ING.getValue());
        customerInstallmentRecord.setTotalMoney(100);
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

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setMoney(100);
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(customerInstallmentRecordPayDetail.getId().toString());
        insertAlipayPayOrder(order);

        payOrderService.foregiftInstallmentMoneyPayOk(order);

        assertEquals(CustomerInstallmentRecord.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_installment_record where id = ?", customerInstallmentRecord.getId()));

    }


    @Test
    public void rentForegiftOrderFirstMoneyPayOk() {
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

        CustomerInstallmentRecord customerInstallmentRecord = newCustomerInstallmentRecord(agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecord.setStatus(CustomerInstallmentRecord.Status.PAY_ING.getValue());
        customerInstallmentRecord.setTotalMoney(100);
        insertCustomerInstallmentRecord(customerInstallmentRecord);

        CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue(), rentForegiftOrder.getId());
        customerForegiftOrderDetail.setNum(1);
        insertCustomerInstallmentRecordOrderDetail(customerForegiftOrderDetail);

        CustomerInstallmentRecordOrderDetail packetPeriodOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue(), rentPeriodOrder.getId());
        packetPeriodOrderOrderDetail.setNum(2);
        insertCustomerInstallmentRecordOrderDetail(packetPeriodOrderOrderDetail);

        RentInsuranceOrder rentInsuranceOrder = newRentInsuranceOrder(customer);
        rentInsuranceOrder.setStatus(RentInsuranceOrder.Status.NOT_PAY.getValue());
        insertRentInsuranceOrder(rentInsuranceOrder);

        CustomerInstallmentRecordOrderDetail insuranceOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue(), rentInsuranceOrder.getId());
        insuranceOrderOrderDetail.setNum(3);
        insertCustomerInstallmentRecordOrderDetail(insuranceOrderOrderDetail);

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setMoney(100);
        customerInstallmentRecordPayDetail.setCategory(2);
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        String sourceId = String.valueOf(customerInstallmentRecordPayDetail.getRecordId());
        order.setSourceId(sourceId);
        insertAlipayPayOrder(order);

        payOrderService.rentForegiftOrderFirstMoneyPayOk(order);

        assertEquals(CustomerInstallmentRecord.Status.PAY_ING.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_installment_record where id = ?", customerInstallmentRecord.getId()));

    }

    @Test
    public void vehicleGroupOrderPayOk() {
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

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
        insertRentPrice(rentPrice);

        VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertVehicleForegiftOrder(vehicleForegiftOrder);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        insertGroupOrder(groupOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(groupOrder.getId().toString());
        insertAlipayPayOrder(order);

        payOrderService.vehicleGroupOrderPayOk(order);

        assertEquals(GroupOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_group_order where id = ?", groupOrder.getId()));
        assertEquals(VehicleForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_foregift_order where id = ?", vehicleForegiftOrder.getId()));
        assertEquals(VehiclePeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ?", vehiclePeriodOrder.getId()));

    }

    @Test
    public void vehicleGroupOrderRentPayOk() {
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

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
        insertRentPrice(rentPrice);

        VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertVehicleForegiftOrder(vehicleForegiftOrder);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        insertGroupOrder(groupOrder);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(groupOrder.getId().toString());
        insertAlipayPayOrder(order);

        payOrderService.vehicleGroupOrderRentPayOk(order);

        assertEquals(GroupOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_group_order where id = ?", groupOrder.getId()));
        assertEquals(VehiclePeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ?", vehiclePeriodOrder.getId()));
        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrder.getId()));

    }

    @Test
    public void multiGroupOrderRentPayOk() {
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
        customerForegiftOrder.setMoney(1000);
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setMoney(1000);
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
        packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setMoney(1000);
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
        insertRentPrice(rentPrice);

        VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        vehicleForegiftOrder.setMoney(1000);
        vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.WAIT_PAY.getValue());
        insertVehicleForegiftOrder(vehicleForegiftOrder);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        vehiclePeriodOrder.setMoney(1000);
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        insertGroupOrder(groupOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setType(CustomerMultiOrder.Type.ZC.getValue());
        customerMultiOrder.setTotalMoney(4000);
        insertCustomerMultiOrder(customerMultiOrder);

        String orderId = "VG123";
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        order.setSourceId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER.getValue() + ":" + groupOrder.getId());
        insertAlipayPayOrder(order);

        payOrderService.multiGroupOrderPayOk(order);

        assertEquals(GroupOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_group_order where id = ?", groupOrder.getId()));
        assertEquals(VehiclePeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ?", vehiclePeriodOrder.getId()));
        assertEquals(VehicleForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_foregift_order where id = ?", vehicleForegiftOrder.getId()));
        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrder.getId()));
        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrder.getId()));
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
        insertCustomerCouponTicket(customerCouponTicket);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setId("111");
        customerForegiftOrder.setMoney(1000);
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setMoney(1000);
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
        packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setMoney(1000);
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
        insertRentPrice(rentPrice);

        VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        vehicleForegiftOrder.setMoney(1000);
        vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.WAIT_PAY.getValue());
        insertVehicleForegiftOrder(vehicleForegiftOrder);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        vehiclePeriodOrder.setMoney(1000);
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
        groupOrder.setMoney(4000);
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        insertGroupOrder(groupOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setType(CustomerMultiOrder.Type.ZC.getValue());
        customerMultiOrder.setTotalMoney(4000);
        insertCustomerMultiOrder(customerMultiOrder);

        int num = 0;
        CustomerMultiOrderDetail customerMultiOrderDetail = newCustomerMultiOrderDetail(customerMultiOrder.getId(), num++, groupOrder.getId(), CustomerMultiOrderDetail.SourceType.ZCGROUP.getValue(), 4000);
        insertCustomerMultiOrderDetail(customerMultiOrderDetail);

        CustomerMultiPayDetail customerMultiPayDetail = newCustomerMultiPayDetail(customerMultiOrder.getId(), ConstEnum.PayType.WEIXIN.getValue(), 4000);
        customerMultiPayDetail.setStatus(CustomerMultiPayDetail.Status.NOT_PAY.getValue());
        customerMultiPayDetail.setPartnerId(partner.getId());
        customerMultiPayDetail.setCustomerId(customer.getId());
        insertCustomerMultiPayDetail(customerMultiPayDetail);

        WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
        weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setPartnerId(customer.getPartnerId());
        weixinPayOrder.setCustomerId(customer.getId());
        weixinPayOrder.setMoney(groupOrder.getMoney());
        weixinPayOrder.setSourceType(PayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue());
        weixinPayOrder.setSourceId(customerMultiPayDetail.getId().toString());
        weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
        weixinPayOrder.setCreateTime(new Date());
        insertWeixinPayOrder(weixinPayOrder);

        payOrderService.multiPayDetailPayOk(weixinPayOrder);

        assertEquals(CustomerMultiPayDetail.Status.HAVE_PAY.getValue(),jdbcTemplate.queryForInt("select status from bas_customer_multi_pay_detail where id = ?", customerMultiPayDetail.getId()));
        assertEquals(CustomerMultiOrder.Status.HAVE_PAY.getValue(),jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id = ?", customerMultiOrder.getId()));
        assertEquals(GroupOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_group_order where id = ?", groupOrder.getId()));
        assertEquals(VehiclePeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ?", vehiclePeriodOrder.getId()));
        assertEquals(VehicleForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_foregift_order where id = ?", vehicleForegiftOrder.getId()));
        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrder.getId()));
        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrder.getId()));
    }
}
