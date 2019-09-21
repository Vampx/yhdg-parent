package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.service.hdg.VipPriceService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class VipRentPriceServiceTest extends BaseJunit4Test {
    @Autowired
    VipRentPriceService vipRentPriceService;

    @Test
    public void findOneByShopId() {
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

        VipRentPeriodPrice vipRentPeriodPrice = newVipRentPeriodPrice(vipRentPrice.getId(), agent.getId(), rentBatteryForegift.getId().longValue());
        insertVipRentPeriodPrice(vipRentPeriodPrice);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        VipRentPriceShop vipRentPriceShop = newVipRentPriceShop(vipRentPrice.getId(), shop.getId());
        insertVipRentPriceShop(vipRentPriceShop);

        assertNotNull(vipRentPriceService.findOneByShopId(vipRentBatteryForegift.getAgentId(), rentBatteryType.getBatteryType(), shop.getId()));
    }

    @Test
    public void findOneByCustomerMobile() {
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

        VipRentPeriodPrice vipRentPeriodPrice = newVipRentPeriodPrice(vipRentPrice.getId(), agent.getId(), rentBatteryForegift.getId().longValue());
        insertVipRentPeriodPrice(vipRentPeriodPrice);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        VipRentPriceCustomer vipRentPriceCustomer = newVipRentPriceCustomer(vipRentPrice.getId());
        insertVipRentPriceCustomer(vipRentPriceCustomer);

        assertNotNull(vipRentPriceService.findOneByCustomerMobile(vipRentBatteryForegift.getAgentId(), rentBatteryType.getBatteryType(), vipRentPriceCustomer.getMobile()));
    }
}
