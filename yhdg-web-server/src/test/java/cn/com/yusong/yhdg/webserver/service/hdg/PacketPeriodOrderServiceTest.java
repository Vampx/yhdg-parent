package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class PacketPeriodOrderServiceTest extends BaseJunit4Test {
    @Autowired
    PacketPeriodOrderService service;

    @Test
    public void find(){

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        assertNotNull(service.find(packetPeriodOrder.getId()));
    }

    @Test
    public void findPage(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        assertTrue(1 == service.findPage(packetPeriodOrder).getTotalItems());
        assertTrue(1 == service.findPage(packetPeriodOrder).getResult().size());
    }

    @Test
    public void findPageForbalance() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);

        assertTrue(1 == service.findPageForbalance(packetPeriodOrder).getTotalItems());
        assertTrue(1 == service.findPageForbalance(packetPeriodOrder).getResult().size());
    }

    @Test
    public void repulseRefund() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setMemo("退款");
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);

        PacketPeriodOrderRefund packetPeriodOrderRefund = newPacketPeriodOrderRefund(packetPeriodOrder.getId(), customer.getId());
        packetPeriodOrderRefund.setRefundStatus(PacketPeriodOrderRefund.RefundStatus.APPLY_REFUND.getValue());
        insertPacketPeriodOrderRefund(packetPeriodOrderRefund);

        assertTrue(service.repulseRefund("asdf", packetPeriodOrder).isSuccess());
        assertEquals(service.find(packetPeriodOrder.getId()).getStatus().intValue(), PacketPeriodOrder.Status.REFUND.getValue());

    }

    @Test
    public void findCanRefundByCustomerId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setMemo("退款");
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);

        assertTrue(1 == service.findCanRefundByCustomerId(customer.getId()).size());
    }
}
