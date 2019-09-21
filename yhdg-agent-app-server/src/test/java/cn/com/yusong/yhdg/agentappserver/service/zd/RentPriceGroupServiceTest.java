package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.zd.RentPriceGroupController;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RentPriceGroupServiceTest extends BaseJunit4Test {

    @Autowired
    RentPriceGroupService priceGroupService;

    @Test
    public void list() {
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

        RentInsurance rentInsurance = newRentInsurance(agent.getId(), rentBatteryType.getBatteryType());
        insertRentInsurance(rentInsurance);

        assertEquals(0, priceGroupService.list(agent.getId()).getCode());

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

        RentInsurance rentInsurance = newRentInsurance(agent.getId(), rentBatteryType.getBatteryType());
        insertRentInsurance(rentInsurance);

        assertEquals(0, priceGroupService.detail(systemBatteryType.getId(), agent.getId()).getCode());

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

        RentInsurance rentInsurance = newRentInsurance(agent.getId(), rentBatteryType.getBatteryType());
        insertRentInsurance(rentInsurance);

        assertEquals(0, priceGroupService.delete(systemBatteryType.getId(), agent.getId()).getCode());

    }

    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentPriceGroupController.CreateParam.Insurance insurance = new RentPriceGroupController.CreateParam.Insurance();
        insurance.insuranceName = "xxxx";
        insurance.price = 3;
        insurance.isActive = 1;
        insurance.money = 1;
        insurance.monthCount = 1;
        insurance.memo = "xxxx";

        RentPriceGroupController.CreateParam.Insurance[] insuranceList = new RentPriceGroupController.CreateParam.Insurance[]{insurance};

        RentPriceGroupController.CreateParam.GroupBiza[] biza = new RentPriceGroupController.CreateParam.GroupBiza[1];
        for (int i = 0; i < 1; i++) {
            RentPriceGroupController.CreateParam.GroupBiza bizaForegift = new RentPriceGroupController.CreateParam.GroupBiza();
            bizaForegift.foregift = 10;

            RentPriceGroupController.CreateParam.GroupBiza.GroupBizaPrice bizaPacketPeriodPrice = new RentPriceGroupController.CreateParam.GroupBiza.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";

            RentPriceGroupController.CreateParam.GroupBiza.GroupBizaPrice[] priceList = new RentPriceGroupController.CreateParam.GroupBiza.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }


        RentBatteryType rentBatteryType = new RentBatteryType();
        rentBatteryType.setAgentId(agent.getId());
        rentBatteryType.setBatteryType(systemBatteryType.getId());
        rentBatteryType.setTypeName(systemBatteryType.getTypeName());

        assertEquals(0,
                priceGroupService.create(rentBatteryType, agent.getId(),
                        insuranceList,
                        biza).getCode());

    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentPriceGroupController.UpdateParam.Insurance insurance = new RentPriceGroupController.UpdateParam.Insurance();
        insurance.insuranceName = "xxxx";
        insurance.price = 3;
        insurance.isActive = 1;
        insurance.money = 1;
        insurance.monthCount = 1;
        insurance.memo = "xxxx";

        RentPriceGroupController.UpdateParam.Insurance[] insuranceList = new RentPriceGroupController.UpdateParam.Insurance[]{insurance};

        RentPriceGroupController.UpdateParam.GroupBiza[] biza = new RentPriceGroupController.UpdateParam.GroupBiza[1];
        for (int i = 0; i < 1; i++) {
            RentPriceGroupController.UpdateParam.GroupBiza bizaForegift = new RentPriceGroupController.UpdateParam.GroupBiza();
            bizaForegift.foregift = 10;

            RentPriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice bizaPacketPeriodPrice = new RentPriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";

            RentPriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice[] priceList = new RentPriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }


        RentBatteryType rentBatteryType = new RentBatteryType();
        rentBatteryType.setAgentId(agent.getId());
        rentBatteryType.setBatteryType(systemBatteryType.getId());
        rentBatteryType.setTypeName(systemBatteryType.getTypeName());

        assertEquals(0,
                priceGroupService.update(rentBatteryType,
                        agent.getId(),
                        insuranceList,
                        biza).getCode());

    }

}
