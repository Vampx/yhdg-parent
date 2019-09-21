package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/8.
 */
public class BatteryOrderRefundServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryOrderRefundSerService batteryOrderRefundSerService;
    @Autowired
    OrderIdService orderIdService;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder("asdfasd", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        BatteryOrderRefund batteryOrderRefund = new BatteryOrderRefund();
        batteryOrderRefund.setId(batteryOrder.getId());
        batteryOrderRefund.setAgentId(agent.getId());
        batteryOrderRefund.setRefundStatus(BatteryOrderRefund.RefundStatus.APPLY_REFUND.getValue());
        batteryOrderRefund.setRefundReason("退货");
        batteryOrderRefund.setApplyRefundTime(new Date());
        batteryOrderRefund.setCreateTime(new Date());
        batteryOrderRefund.setCustomerId(customer.getId());
        batteryOrderRefund.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        batteryOrderRefund.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        int result = batteryOrderRefundSerService.insert(batteryOrderRefund);
        assertEquals(1, result);
    }
}
