package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.service.hdg.VipExchangeBatteryForegiftService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.VipRentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class VipRentBatteryForegiftServiceTest extends BaseJunit4Test {
    @Autowired
    VipRentBatteryForegiftService vipRentBatteryForegiftService;

    @Test
    public void findByPriceId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        RentBatteryType rentBatteryType = newRentBatteryType(agent.getId(), battery.getType());
        insertRentBatteryType(rentBatteryType);

        RentBatteryForegift rentBatteryForegift = newRentBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertRentBatteryForegift(rentBatteryForegift);

        VipRentPrice vipRentPrice = newVipRentPrice(agent.getId(), systemBatteryType.getId());
        insertVipRentPrice(vipRentPrice);

        VipRentBatteryForegift vipRentBatteryForegift = newVipRentBatteryForegift(agent.getId(), rentBatteryForegift.getId().longValue(), vipRentPrice.getId());
        insertVipRentBatteryForegift(vipRentBatteryForegift);

        assertEquals(1, vipRentBatteryForegiftService.findByPriceId(vipRentPrice.getId()).size());
    }
}
