package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class CabinetBoxServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetBoxService cabinetBoxService;

    @Test
    public void findAllEmpty() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.findAllEmpty(cabinet.getId(), CabinetBox.BoxStatus.EMPTY.getValue()));
    }

    @Test
    public void findAllNotFull() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.findAllNotFull(cabinet.getId(), CabinetBox.BoxStatus.FULL.getValue()));
    }

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.findList(cabinet.getId()));
    }

    @Test
    public void findOneEmptyBoxNum() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        assertNotNull(cabinetBoxService.findOneEmptyBoxNum(cabinet.getId(), battery.getType()));
    }

    @Test
    public void findOneFull() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(100);
        battery.setUpLineStatus(1);
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setBatteryId(battery.getId());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(cabinetBox);

        assertNotNull(cabinetBoxService.findOneFull(cabinet.getId(), battery.getType(), null));

    }

    /**
     * 包含预约格口  预约格口电量大于其他格口
     */
    @Test
    public void findOneFull_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(90);
        battery.setUpLineStatus(1);
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setBatteryId(battery.getId());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(cabinetBox);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setVolume(100);
        battery1.setUpLineStatus(1);
        battery1.setId("3334");
        battery1.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery1);

        CabinetBox cabinetBox1= newCabinetBox(cabinet.getId(), "0-2");
        cabinetBox1.setBatteryId(battery1.getId());
        cabinetBox1.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(cabinetBox1);

        CabinetBox box = cabinetBoxService.findOneFull(cabinet.getId(), battery.getType(), cabinetBox1.getBoxNum());
        assertEquals("0-2",box.getBoxNum());
    }

    /**
     * 只有预约格口
     */
    @Test
    public void findOneFull_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(90);
        battery.setUpLineStatus(0);
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        cabinetBox.setBatteryId(battery.getId());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(cabinetBox);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setVolume(80);
        battery1.setUpLineStatus(1);
        battery1.setId("3334");
        battery1.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery1);

        CabinetBox cabinetBox1= newCabinetBox(cabinet.getId(), "0-2");
        cabinetBox1.setBatteryId(battery1.getId());
        cabinetBox1.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(cabinetBox1);

        CabinetBox box = cabinetBoxService.findOneFull(cabinet.getId(), battery.getType(), cabinetBox1.getBoxNum());
        assertEquals("0-2",box.getBoxNum());
    }

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.find(cabinet.getId(), cabinetBox.getBoxNum()));
    }


    @Test
    public void findEmptyCount() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.findEmptyCount(cabinet.getId()));
    }

    @Test
    public void findFullCount() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.findFullCount(cabinet.getId()));
    }

    @Test
    public void lockBox() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.lockBox(cabinet.getId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.EMPTY.getValue(), CabinetBox.BoxStatus.EMPTY_LOCK.getValue()));
    }

    @Test
    public void updateOpenType() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);
        assertNotNull(cabinetBoxService.updateOpenType(cabinet.getId(), cabinetBox.getBoxNum(), CabinetBox.OpenType.KEEP_ORDER_OPEN_FULL_BOX.getValue(), user.getId()));
    }
}
