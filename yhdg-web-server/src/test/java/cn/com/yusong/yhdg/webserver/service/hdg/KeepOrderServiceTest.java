package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.protocol.msg08.HeartParam;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


public class KeepOrderServiceTest extends BaseJunit4Test {
    @Autowired
    KeepOrderService service;

    @Test
    public void find(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        KeepOrder keepOrder = newKeepOrder(newOrderId(OrderId.OrderIdType.KEEP_ORDER), agent.getId(), battery.getId());
        insertKeepOrder(keepOrder);

        assertNotNull(service.find(keepOrder.getId()));
    }

    @Test
    public void findPage(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        KeepOrder keepOrder = newKeepOrder(newOrderId(OrderId.OrderIdType.KEEP_ORDER), agent.getId(), battery.getId());
        insertKeepOrder(keepOrder);

        assertTrue(1 == service.findPage(keepOrder).getTotalItems());
        assertTrue(1 == service.findPage(keepOrder).getResult().size());
    }
}
