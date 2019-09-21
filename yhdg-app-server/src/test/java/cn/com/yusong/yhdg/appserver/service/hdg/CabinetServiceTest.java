package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CabinetServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetService cabinetService;

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

        assertNotNull(cabinetService.find(cabinet.getId()));
    }


    @Test
    public void findNearest() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);
        cabinet.setGeoHash(GeoHashUtils.getGeoHashString(cabinet.getLng(), cabinet.getLat()));
        cabinet.setActiveStatus(Cabinet.ActiveStatus.ENABLE.getValue());
        cabinet.setKeyword("zzz");
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        insertCabinet(cabinet);
        String geoHash = GeoHashUtils.getGeoHashString(cabinet.getLng(), cabinet.getLat());
        List<Integer> list = new ArrayList<Integer>();
        list.add(agent.getId());
        assertFalse(cabinetService.findNearest(list, geoHash.substring(0, 5), 120.021724, 30.35371, "zzz", null, null, null,null,0, 100).isEmpty());
        double distance = cabinetService.findNearest(null, null, 120.021724, 30.35371, "zzz", null, null, null,null,0, 100).get(0).getDistance();
        assertEquals(0.0, distance);


    }

    @Test
    public void findList() {
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
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        assertNotNull(cabinetService.findList(user.getId()));
    }


    @Test
    public void findBatteryCountByDispatcher() {

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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        assertEquals(1, cabinetService.findBatteryCountByDispatcher(user.getId()));
    }

    @Test
    public void findCountByDispatcher() {
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
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);


        Cabinet cabinet1 = newCabinet(agent.getId(), terminal.getId());
        cabinet1.setId("000002");
        cabinet1.setDispatcherId(user.getId());
        insertCabinet(cabinet1);


        assertEquals(2, cabinetService.findCountByDispatcher(user.getId()));
    }

    @Test
    public void findNotFullBatteryCountByDispatcher() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(60);
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        cabinet.setChargeFullVolume(100);
        insertCabinet(cabinet);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        assertEquals(1, cabinetService.findNotFullBatteryCountByDispatcher(user.getId()));
    }

    @Test
    public void findBatteryCountByCabinet() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        assertEquals(1, cabinetService.findBatteryCountByCabinet(cabinet.getId()));
    }

    @Test
    public void findNotFullBatteryCountByCabinet() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(60);
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setChargeFullVolume(90);
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        assertEquals(1, cabinetService.findNotFullBatteryCountByCabinet(cabinet.getId()));
    }

    @Test
    public void findOnlineSubcabinetCountByCabinet() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(1);
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        assertEquals(1, cabinetService.findOnlineSubcabinetCountByCabinet(cabinet.getId()));
    }

    @Test
    public void findOfflineSubcabinetCountByCabinet() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        assertEquals(1, cabinetService.findOfflineSubcabinetCountByCabinet(cabinet.getId()));
    }

    @Test
    public void findSubcabinetList() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);
    }

    @Test
    public void subcabinetBattery() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);
        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

    }

    @Test
    public void subcabinetBattery1() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);


    }

    @Test
    public void updateToken() {
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        assertEquals(1, cabinetService.updateToken(cabinet.getId(), "token:11223355658"));

        assertEquals("token:11223355658", jdbcTemplate.queryForMap("select login_token from hdg_cabinet where id = ?", cabinet.getId()).get("login_token"));
    }
}
