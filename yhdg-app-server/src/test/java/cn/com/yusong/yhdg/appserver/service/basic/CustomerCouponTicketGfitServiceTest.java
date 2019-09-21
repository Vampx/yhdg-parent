package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/12/16.
 */
public class CustomerCouponTicketGfitServiceTest extends BaseJunit4Test {

    @Autowired
    AbstractService abstractService;

    @Test
    public void giveTicket1() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setType(CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(entity);

        String sourceId = entity.getId();
        Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();
        Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();

        abstractService.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type,0, agent.getId(), 1, customer.getMobile());
    }

    @Test
    public void giveTicket2() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setType(CustomerCouponTicketGift.Type.BUY_RENT.getValue());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setDayCount(5);
        insertPacketPeriodOrder(packetPeriodOrder);

        String sourceId = packetPeriodOrder.getId();
        Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();
        Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

        abstractService.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type,packetPeriodOrder.getDayCount(), agent.getId(), 2, customer.getMobile());
    }
}
