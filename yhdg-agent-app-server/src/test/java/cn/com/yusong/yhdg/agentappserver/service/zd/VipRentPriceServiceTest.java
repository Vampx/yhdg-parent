package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.agentappserver.service.hdg.VipPriceService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.VipPriceController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.zd.VipRentPriceController;
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
    public void list() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        VipRentPrice vipRentPrice = newVipRentPrice(agent.getId(), systemBatteryType.getId());
        insertVipRentPrice(vipRentPrice);

        assertEquals(0, vipRentPriceService.list(agent.getId(), "", 0, 10).getCode());

    }

    @Test
    public void detail() {
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

        VipPriceCustomer vipPriceCustomer = newVipPriceCustomer(vipRentPrice.getId());
        insertVipPriceCustomer(vipPriceCustomer);

        assertEquals(0, vipRentPriceService.detail(vipRentPrice.getId(), agent.getId()).getCode());
    }

    @Test
    public void create() {
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

        VipRentPrice vipPrice = newVipRentPrice(agent.getId(), systemBatteryType.getId());

        VipRentPriceController.CreateParam.VipPacketPeriodPrice[] biza = new VipRentPriceController.CreateParam.VipPacketPeriodPrice[1];
        for (int i = 0; i < 1; i++) {
            VipRentPriceController.CreateParam.VipPacketPeriodPrice bizaForegift = new VipRentPriceController.CreateParam.VipPacketPeriodPrice();
            bizaForegift.foregiftId = 1L;
            bizaForegift.reduceMoney = 10;

            VipRentPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPacketPeriodPrice = new VipRentPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";

            VipRentPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice[] priceList = new VipRentPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        String[] customerMobileList = new String[] {"15145252526"};

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        insertAgentCompany(agentCompany);

        String[] shopIdList = new String[] {shop.getId()};
        String[] agentCompanyIdList = new String[] {agentCompany.getId()};

        assertEquals(0,
                vipRentPriceService.create(vipPrice,
                        agent.getId(), biza, customerMobileList, shopIdList, agentCompanyIdList).getCode());

    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipRentPrice vipRentPrice = newVipRentPrice(agent.getId(), systemBatteryType.getId());
        insertVipRentPrice(vipRentPrice);

        VipRentPriceController.UpdateParam.VipPacketPeriodPrice[] biza = new VipRentPriceController.UpdateParam.VipPacketPeriodPrice[1];
        for (int i = 0; i < 1; i++) {
            VipRentPriceController.UpdateParam.VipPacketPeriodPrice bizaForegift = new VipRentPriceController.UpdateParam.VipPacketPeriodPrice();
            bizaForegift.foregiftId = 1L;
            bizaForegift.reduceMoney = 10;

            VipRentPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPacketPeriodPrice = new VipRentPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";
            bizaPacketPeriodPrice.limitCount = 1;
            bizaPacketPeriodPrice.dayLimitCount = 1;

            VipRentPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice[] priceList = new VipRentPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        String[] customerMobileList = new String[] {"15145252526"};

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        insertAgentCompany(agentCompany);

        String[] shopIdList = new String[] {shop.getId()};
        String[] agentCompanyIdList = new String[] {agentCompany.getId()};

        assertEquals(0,
                vipRentPriceService.update(vipRentPrice,
                        agent.getId(), biza, customerMobileList, shopIdList, agentCompanyIdList).getCode());

    }


    @Test
    public void delete() {
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

        assertEquals(0, vipRentPriceService.delete(vipRentPrice.getId()).getCode());
    }

    @Test
    public void vipShopDelete() {
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

        VipRentPriceCustomer vipRentPriceCustomer = newVipRentPriceCustomer(vipRentPrice.getId());
        insertVipRentPriceCustomer(vipRentPriceCustomer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        VipRentPriceShop vipRentPriceShop = newVipRentPriceShop(vipRentPrice.getId(), shop.getId());
        insertVipRentPriceShop(vipRentPriceShop);

        assertEquals(0, vipRentPriceService.vipShopDelete(vipRentPriceShop.getId()).getCode());
    }


    @Test
    public void vipCustomerDelete() {
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

        VipRentPriceCustomer vipRentPriceCustomer = newVipRentPriceCustomer(vipRentPrice.getId());
        insertVipRentPriceCustomer(vipRentPriceCustomer);

        assertEquals(0, vipRentPriceService.vipCustomerDelete(vipRentPriceCustomer.getId()).getCode());
    }
}
