package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.service.hdg.VipPacketPeriodPriceService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class VipRentPeriodPriceServiceTest extends BaseJunit4Test {
    @Autowired
    VipRentPeriodPriceService vipRentPeriodPriceService;

    @Test
    public void findCountByForegiftId() {
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

        VipRentPeriodPrice vipPacketPeriodPrice = newVipRentPeriodPrice(vipRentPrice.getId(), agent.getId(), vipRentBatteryForegift.getId());
        insertVipRentPeriodPrice(vipPacketPeriodPrice);

        assertEquals(1, vipRentPeriodPriceService.findCountByForegiftId(vipRentPrice.getId(), vipRentBatteryForegift.getId()));
    }


    @Test
    public void findByPriceIdAndForegiftId() {
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

        VipRentPeriodPrice vipPacketPeriodPrice = newVipRentPeriodPrice(vipRentPrice.getId(), agent.getId(), vipRentBatteryForegift.getId());
        insertVipRentPeriodPrice(vipPacketPeriodPrice);

        assertEquals(1, vipRentPeriodPriceService.findByPriceIdAndForegiftId(vipRentPrice.getId(), vipRentBatteryForegift.getId()).size());
    }
}
