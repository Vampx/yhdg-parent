package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PushMetaDataServiceTest extends BaseJunit4Test {
    @Autowired
    PushMetaDataService pushMetaDataService;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setPushToken("asdasdasd");
        customer.setPushType(ConstEnum.PushType.XIAOMI.getValue());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder("AA987987987987", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPrice(1000);
        insertBatteryOrder(batteryOrder);
        PushMetaData pushMetaData = newPushMetaData(batteryOrder.getId(), PushMessage.SourceType.BATTERY_ORDER_NOT_TAKE_TIMEOUT.getValue());

        pushMetaDataService.insert(pushMetaData);
    }
}
