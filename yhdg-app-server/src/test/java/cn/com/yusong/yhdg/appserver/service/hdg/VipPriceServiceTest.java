package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class VipPriceServiceTest extends BaseJunit4Test {
    @Autowired
    VipPriceService vipPriceService;

    @Test
    public void findOneByStationId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), exchangeBatteryForegift.getId().longValue(), vipPrice.getId());
        vipExchangeBatteryForegift.setReduceMoney(10);
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(),vipPrice.getId(), agent.getId(), vipExchangeBatteryForegift.getId().longValue());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        Station station = newStation(agent.getId());
        insertStation(station);

        VipPriceStation vipPriceStation = newVipPriceStation(vipPrice.getId(), station.getId());
        insertVipPriceStation(vipPriceStation);

        assertNotNull(vipPriceService.findOneByStationId(vipExchangeBatteryForegift.getAgentId(), agentBatteryType.getBatteryType(), station.getId()));
    }


    @Test
    public void findOneByCabinetId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), exchangeBatteryForegift.getId().longValue(), vipPrice.getId());
        vipExchangeBatteryForegift.setReduceMoney(10);
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(),vipPrice.getId(), agent.getId(), vipExchangeBatteryForegift.getId().longValue());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        VipPriceCabinet vipPriceCabinet = newVipPriceCabinet(vipPrice.getId(), cabinet.getId());
        insertVipPriceCabinet(vipPriceCabinet);

        assertNotNull(vipPriceService.findOneByCabinetId(vipExchangeBatteryForegift.getAgentId(), agentBatteryType.getBatteryType(), vipPriceCabinet.getCabinetId()));
    }

    @Test
    public void findOneByCustomerMobile() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), exchangeBatteryForegift.getId().longValue(), vipPrice.getId());
        vipExchangeBatteryForegift.setReduceMoney(10);
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(),vipPrice.getId(), agent.getId(), vipExchangeBatteryForegift.getId().longValue());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        VipPriceCustomer vipPriceCustomer = newVipPriceCustomer(vipPrice.getId());
        insertVipPriceCustomer(vipPriceCustomer);

        assertNotNull(vipPriceService.findOneByCustomerMobile(vipExchangeBatteryForegift.getAgentId(), agentBatteryType.getBatteryType(), vipPriceCustomer.getMobile()));
    }
}
