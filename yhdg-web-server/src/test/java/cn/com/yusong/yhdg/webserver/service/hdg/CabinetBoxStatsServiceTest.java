package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.CabinetBoxStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CabinetBoxStatsServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetBoxStatsService service;

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);



        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCode("00001");
        battery1.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        insertBattery(battery1);

        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("000002");
        battery2.setCode("00002");
        battery2.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        insertBattery(battery2);

        Battery battery3 = newBattery(agent.getId(), systemBatteryType.getId());
        battery3.setId("000003");
        battery2.setCode("00003");
        battery3.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery3);

        Battery battery4 = newBattery(agent.getId(), systemBatteryType.getId());
        battery4.setId("000004");
        battery4.setCode("00004");
        battery4.setStatus(Battery.Status.IN_BOX_NOT_PAY.getValue());
        insertBattery(battery4);

        Battery battery5 = newBattery(agent.getId(), systemBatteryType.getId());
        battery5.setId("000005");
        battery5.setCode("00005");
        battery5.setStatus(Battery.Status.IN_BOX_CUSTOMER_USE.getValue());
        insertBattery(battery5);

        CabinetBox cabinetBox1 = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox1.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox1);

        CabinetBox cabinetBox2 = newCabinetBox(cabinet.getId(), "0-2");
        cabinetBox2.setIsOpen(ConstEnum.Flag.TRUE.getValue());
        cabinetBox2.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox2);

        CabinetBox cabinetBox3 = newCabinetBox(cabinet.getId(), "0-3");
        cabinetBox3.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        cabinetBox3.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox3);

        CabinetBox cabinetBox4 = newCabinetBox(cabinet.getId(), "0-4");
        cabinetBox4.setBatteryId(battery1.getId());
        cabinetBox4.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox4);

        CabinetBox cabinetBox5 = newCabinetBox(cabinet.getId(), "0-5");
        cabinetBox5.setBatteryId(battery2.getId());
        cabinetBox5.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox5);

        CabinetBox cabinetBox6 = newCabinetBox(cabinet.getId(), "0-6");
        cabinetBox6.setBatteryId(battery3.getId());
        cabinetBox6.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox6);

        CabinetBox cabinetBox7 = newCabinetBox(cabinet.getId(), "0-7");
        cabinetBox7.setBatteryId(battery4.getId());
        cabinetBox7.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox7);

        CabinetBox cabinetBox8 = newCabinetBox(cabinet.getId(), "0-8");
        cabinetBox8.setBatteryId(battery5.getId());
        cabinetBox8.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox8);

        Page page = service.findPage(cabinet);
        List<CabinetBoxStats> result = page.getResult();
        for (CabinetBoxStats cabinetBoxStats : result) {
            assertTrue(cabinetBoxStats.getBoxCount() > 0);
            assertTrue(cabinetBoxStats.getEmptyCount() > 0);
            assertTrue(cabinetBoxStats.getOpenCount() > 0);
            assertTrue(cabinetBoxStats.getBatteryCount() > 0);
            assertTrue(cabinetBoxStats.getChargingCount() > 0);
            assertTrue(cabinetBoxStats.getWaitChargeCount() > 0);
            assertTrue(cabinetBoxStats.getCompleteChargeCount() > 0);
            assertTrue(cabinetBoxStats.getNotPayCount() > 0);
            assertTrue(cabinetBoxStats.getNotTakeCount() > 0);
        }
    }
    @Test
    public void findList() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCode("00001");
        battery1.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        insertBattery(battery1);

        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("000002");
        battery2.setCode("00002");
        battery2.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        insertBattery(battery2);

        Battery battery3 = newBattery(agent.getId(), systemBatteryType.getId());
        battery3.setId("000003");
        battery2.setCode("00003");
        battery3.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery3);

        Battery battery4 = newBattery(agent.getId(), systemBatteryType.getId());
        battery4.setId("000004");
        battery4.setCode("00004");
        battery4.setStatus(Battery.Status.IN_BOX_NOT_PAY.getValue());
        insertBattery(battery4);

        Battery battery5 = newBattery(agent.getId(), systemBatteryType.getId());
        battery5.setId("000005");
        battery5.setCode("00005");
        battery5.setStatus(Battery.Status.IN_BOX_CUSTOMER_USE.getValue());
        insertBattery(battery5);

        CabinetBox cabinetBox1 = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox1.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox1);

        CabinetBox cabinetBox2 = newCabinetBox(cabinet.getId(), "0-2");
        cabinetBox2.setIsOpen(ConstEnum.Flag.TRUE.getValue());
        cabinetBox2.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox2);

        CabinetBox cabinetBox3 = newCabinetBox(cabinet.getId(), "0-3");
        cabinetBox3.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        cabinetBox3.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox3);

        CabinetBox cabinetBox4 = newCabinetBox(cabinet.getId(), "0-4");
        cabinetBox4.setBatteryId(battery1.getId());
        cabinetBox4.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox4);

        CabinetBox cabinetBox5 = newCabinetBox(cabinet.getId(), "0-5");
        cabinetBox5.setBatteryId(battery2.getId());
        cabinetBox5.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox5);

        CabinetBox cabinetBox6 = newCabinetBox(cabinet.getId(), "0-6");
        cabinetBox6.setBatteryId(battery3.getId());
        cabinetBox6.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox6);

        CabinetBox cabinetBox7 = newCabinetBox(cabinet.getId(), "0-7");
        cabinetBox7.setBatteryId(battery4.getId());
        cabinetBox7.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox7);

        CabinetBox cabinetBox8 = newCabinetBox(cabinet.getId(), "0-8");
        cabinetBox8.setBatteryId(battery5.getId());
        cabinetBox8.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox8);

        List<CabinetBoxStats> result = service.findList(cabinet);
        for (CabinetBoxStats cabinetBoxStats : result) {
            assertTrue(cabinetBoxStats.getBoxCount() > 0);
            assertTrue(cabinetBoxStats.getEmptyCount() > 0);
            assertTrue(cabinetBoxStats.getOpenCount() > 0);
            assertTrue(cabinetBoxStats.getBatteryCount() > 0);
            assertTrue(cabinetBoxStats.getChargingCount() > 0);
            assertTrue(cabinetBoxStats.getWaitChargeCount() > 0);
            assertTrue(cabinetBoxStats.getCompleteChargeCount() > 0);
            assertTrue(cabinetBoxStats.getNotPayCount() > 0);
            assertTrue(cabinetBoxStats.getNotTakeCount() > 0);
        }
    }

    @Test
    public void statsBoxCountByStatusAndAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);




        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);



        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCode("00001");
        battery1.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        insertBattery(battery1);

        CabinetBox cabinetBox1 = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox1.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        cabinetBox1.setBatteryId(battery1.getId());
        cabinetBox1.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox1);

        assertTrue(1 == service.statsBoxCountByStatusAndAgent(cabinetBox1.getAgentId(), cabinetBox1.getType()));

    }

    @Test
    public void statsCountByChargeStatusAndAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCode("00001");
        battery1.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        insertBattery(battery1);

        CabinetBox cabinetBox1 = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox1.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        cabinetBox1.setBatteryId(battery1.getId());
        cabinetBox1.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox1);

        assertTrue(1 == service.statsCountByChargeStatusAndAgent(cabinetBox1.getAgentId(), cabinetBox1.getType()));

    }

    @Test
    public void statsCompleteChargeCountAndAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCode("00001");
        battery1.setStatus(Battery.Status.IN_BOX.getValue());
        battery1.setVolume(cabinet.getChargeFullVolume());
        insertBattery(battery1);

        CabinetBox cabinetBox1 = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox1.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        cabinetBox1.setBatteryId(battery1.getId());
        cabinetBox1.setAgentId(agent.getId());
        cabinetBox1.setCabinetId(cabinet.getId());
        insertCabinetBox(cabinetBox1);

        assertEquals(1 , service.statsCompleteChargeCountAndAgent(cabinetBox1.getAgentId(), cabinetBox1.getType()));

    }

    @Test
    public void statsPage1() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(1);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage2() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(2);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage3() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setIsOpen(ConstEnum.Flag.TRUE.getValue());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(3);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage4() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(4);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage5() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(5);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage6() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(6);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage7() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(7);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage8() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        battery.setStatus(Battery.Status.IN_BOX_NOT_PAY.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(8);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }

    @Test
    public void statsPage9() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode("00001");
        battery.setStatus(Battery.Status.IN_BOX_CUSTOMER_USE.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setAgentId(agent.getId());
        cabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(cabinetBox);

        cabinetBox.setViewFlag(9);
        List<CabinetBox> result = service.statsPage(cabinetBox).getResult();
        assertTrue(result.size() > 0);
    }


}
