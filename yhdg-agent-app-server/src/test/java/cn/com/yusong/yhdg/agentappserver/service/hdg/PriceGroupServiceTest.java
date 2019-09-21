package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.PriceGroupController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.apache.ibatis.annotations.Insert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class PriceGroupServiceTest extends BaseJunit4Test {

    @Autowired
    PriceGroupService priceGroupService;

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

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

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

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

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

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

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

        PriceGroupController.CreateParam.GroupBiza[] biza = new PriceGroupController.CreateParam.GroupBiza[1];
        for (int i = 0; i < 1; i++) {
            PriceGroupController.CreateParam.GroupBiza bizaForegift = new PriceGroupController.CreateParam.GroupBiza();
            bizaForegift.foregift = 10;

            PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice bizaPacketPeriodPrice = new PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";
            bizaPacketPeriodPrice.isTicket = 1;
            bizaPacketPeriodPrice.limitCount = 1;
            bizaPacketPeriodPrice.dayLimitCount = 1;


            PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew bizaPacketPeriodPriceRenew = new PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew();
            bizaPacketPeriodPriceRenew.dayCount = 12;
            bizaPacketPeriodPriceRenew.price = 13;
            bizaPacketPeriodPriceRenew.memo = "xzxxx";
            bizaPacketPeriodPriceRenew.isTicket = 1;
            bizaPacketPeriodPriceRenew.limitCount = 11;
            bizaPacketPeriodPriceRenew.dayLimitCount = 11;

            PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew[] priceRenewList = new PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew[]{bizaPacketPeriodPriceRenew};

            bizaPacketPeriodPrice.priceListRenew = priceRenewList;

            PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice[] priceList = new PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }

        AgentBatteryType agentBatteryType = new AgentBatteryType();
        agentBatteryType.setAgentId(agent.getId());
        agentBatteryType.setBatteryType(systemBatteryType.getId());
        agentBatteryType.setTypeName(systemBatteryType.getTypeName());

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        String[] cabinetIdList = {cabinet.getId()};

        Station station = newStation(agent.getId());
        insertStation(station);

        String[] stationIdList = new String[] {station.getId()};

        assertEquals(0,
                priceGroupService.create(agentBatteryType,
                        agent.getId(), 1, 10,
                        biza, cabinetIdList, stationIdList).getCode());

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

        PriceGroupController.UpdateParam.GroupBiza[] biza = new PriceGroupController.UpdateParam.GroupBiza[1];
        for (int i = 0; i < 1; i++) {
            PriceGroupController.UpdateParam.GroupBiza bizaForegift = new PriceGroupController.UpdateParam.GroupBiza();
            bizaForegift.foregift = 10;

            PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice bizaPacketPeriodPrice = new PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";
            bizaPacketPeriodPrice.isTicket = 1;
            bizaPacketPeriodPrice.limitCount = 1;
            bizaPacketPeriodPrice.dayLimitCount = 1;


            PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew bizaPacketPeriodPriceRenew = new PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew();
            bizaPacketPeriodPriceRenew.dayCount = 12;
            bizaPacketPeriodPriceRenew.price = 13;
            bizaPacketPeriodPriceRenew.memo = "xzxxx";
            bizaPacketPeriodPriceRenew.isTicket = 1;
            bizaPacketPeriodPriceRenew.limitCount = 11;
            bizaPacketPeriodPriceRenew.dayLimitCount = 11;

            PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew[] priceRenewList = new PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew[]{bizaPacketPeriodPriceRenew};

            bizaPacketPeriodPrice.priceListRenew = priceRenewList;

            PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice[] priceList = new PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }

        AgentBatteryType agentBatteryType = new AgentBatteryType();
        agentBatteryType.setAgentId(agent.getId());
        agentBatteryType.setBatteryType(systemBatteryType.getId());
        agentBatteryType.setTypeName(systemBatteryType.getTypeName());

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        String[] cabinetIdList = {cabinet.getId()};

        Station station = newStation(agent.getId());
        insertStation(station);

        String[] stationIdList = new String[] {station.getId()};

        assertEquals(0,
                priceGroupService.update(agentBatteryType,
                        agent.getId(), 1, 10,
                        biza, cabinetIdList, stationIdList).getCode());

    }

}
