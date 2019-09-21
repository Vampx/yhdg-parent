package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class VipExchangeBatteryForegiftServiceTest extends BaseJunit4Test {
    @Autowired
    VipExchangeBatteryForegiftService vipExchangeBatteryForegiftService;

    @Test
    public void findByPriceId() {
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

        assertEquals(1, vipExchangeBatteryForegiftService.findByPriceId(vipPrice.getId()).size());
    }
}
