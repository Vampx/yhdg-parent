package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class CustomerRentInfoServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerRentInfoService customerRentInfoService;

    @Test
    public void find() {
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

        assertNotNull(customerRentInfoService.find(customerRentInfo.getId()));
    }
}