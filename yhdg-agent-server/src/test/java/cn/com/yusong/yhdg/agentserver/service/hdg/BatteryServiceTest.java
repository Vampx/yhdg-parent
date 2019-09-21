package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.agentserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;


public class BatteryServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryService service;
    @Autowired
    SystemConfigMapper systemConfigMapper;


    @Test
    public void findPage() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        assertTrue(1 == service.findPage(battery).getTotalItems());
        assertTrue(1 == service.findPage(battery).getResult().size());
    }

    @Test
    public void findBusinessBatteryPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setId("1");
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "asdf");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        assertTrue(1 == service.findBusinessBatteryPage(battery).getTotalItems());
        assertTrue(1 == service.findBusinessBatteryPage(battery).getResult().size());
    }

    @Test
    public void findShopBatteryPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        assertTrue(1 == service.findShopBatteryPage(battery).getTotalItems());
        assertTrue(1 == service.findShopBatteryPage(battery).getResult().size());
    }

    @Test
    public void findForExcel() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        assertTrue(service.findForExcel(battery).size() == 1);
    }

    @Test
    public void findSingleVoltage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        assertEquals(battery.getSingleVoltage(), service.findSingleVoltage(battery.getId()));
    }

    @Test
    public void findCountByPositionState() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        assertTrue(1 == service.findCountByPositionState(battery.getPositionState(), battery.getIsOnline(), battery
                .getAgentId()));

    }

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        assertTrue(1 == service.findList(battery).getTotalItems());
        assertTrue(1 == service.findList(battery).getResult().size());
    }

    @Test
    public void find() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        assertNotNull(service.find(battery.getId()));
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
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        assertTrue(service.create(battery).isSuccess());
    }

    @Test
    public void findUnique() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("22222");
        assertTrue(service.findUnique(battery2.getId()).isSuccess());
    }

    @Test
    public void findUniqueCode() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("22222");
        battery2.setCode("200");
        assertEquals(true, service.findUniqueCode(battery2.getCode(), null).isSuccess());
    }

    @Test
    public void findQrcode() {

        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        assertTrue(service.findQrcode(battery.getCode(), battery.getId()).isSuccess());
    }

    @Test
    public void findShellCode() {

        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        assertTrue(service.findShellCode(battery.getShellCode(), battery.getId()).isSuccess());
    }

    @Test
    public void update() {

        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        battery.setSimMemo("15145256256");
        assertTrue(service.update(battery).isSuccess());
    }

    @Test
    public void delete() {

        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        assertTrue(service.delete(battery.getId()).isSuccess());
    }

    @Test
    public void btchImportBattery() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        File file = new File("C:/Users/Administrator/Desktop/Battery.xls");
        service.btchImportBattery(file, agent.getId());
    }

    @Test
    public void findCount() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        battery1.setChargeCompleteVolume(100);
        battery1.setVolume(10);
        insertBattery(battery1);//未完成充电数量1//未使用电池数量1

        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        battery2.setChargeCompleteVolume(100);
        battery2.setVolume(120);
        battery2.setId("asdf234");
        battery2.setCode("afds34");
        insertBattery(battery2);//满电数1
        //总电池数2

        BatteryStatis batteryStatis = service.findCount(agent.getId(), Battery.ChargeStatus.WAIT_CHARGE.getValue());
        assertTrue(1 == batteryStatis.getChargingCount());
        assertTrue(1 == batteryStatis.getFullCount());
        assertTrue(1 == batteryStatis.getWaitChargeCount());
        assertTrue(2 == batteryStatis.getFactoryBatteryCount());
    }

    @Test
    public void findByAgent() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        insertBattery(battery);

        assertTrue(1 == service.findByAgent(agent.getId(), battery.getId(), 3).size());
    }

    @Test
    public void boundCard() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        String agentId = systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.TEST_AGENT.getValue());
        agent.setId(Integer.parseInt(agentId));
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        insertBattery(battery);

        assertTrue(service.boundCard(battery).isSuccess());
    }

    @Test
    public void updateFullVolume() {

        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setId("456");
        battery1.setCode("456456");
        insertBattery(battery1);

        String[] ids = {battery.getId(), battery1.getId()};

        assertTrue(service.updateFullVolume(ids, 95).isSuccess());
//      assertEquals(95, jdbcTemplate.queryForMap("select charge_complete_volume from hdg_battery where id = ?",
// battery1.getId()).get("full_volume"));
        for (int i = 0; i < ids.length; i++) {
            Integer chargeCompleteVolume = service.find(ids[i]).getChargeCompleteVolume();
            assertTrue(95 == chargeCompleteVolume);
        }
    }

//    @Test
//    public void getTotalBatterCount() {
//        Customer customer = newCustomer(partner.getId());
//        insertCustomer(customer);
//
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Battery battery = newBattery(agent.getId());
//        insertBattery(battery);
//
//        assertTrue(1 == service.getTotalBatterCount());
//    }
//
//    @Test
//    public void getCustomerBatteryCount() {
//        Customer customer = newCustomer(partner.getId());
//        insertCustomer(customer);
//
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Battery battery = newBattery(agent.getId());
//        battery.setCustomerId(customer.getId());
//        insertBattery(battery);
//
//        assertTrue(1 == service.getCustomerBatteryCount());
//    }

}
