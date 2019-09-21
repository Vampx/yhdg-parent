package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class FaultLogServiceTest extends BaseJunit4Test {
    final String suffix = DateFormatUtils.format(new Date(), "yyyyww");

    @Autowired
    FaultLogService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        assertNotNull(service.find(faultLog.getId()));
    }

    @Test
    public void findCount() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        assertTrue(1 == service.findCount(faultLog.getStatus()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        assertTrue(1 == service.findPage(faultLog).getTotalItems());
        assertTrue(1 == service.findPage(faultLog).getResult().size());
    }

    @Test
    public void updateStatus() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(1L);

        //CODE_3
        FaultLog faultLog3 = newFaultLog(agent.getId());
        faultLog3.setDispatcherId(1L);
        faultLog3.setCabinetId(cabinet.getId());
        faultLog3.setFaultType(FaultLog.FaultType.CODE_3.getValue());
        faultLog3.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog3);

        cabinet.setAllFullCount(8);
        cabinet.setAllFullFaultLogId(faultLog3.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        //CODE_2
        FaultLog faultLog1 = newFaultLog(agent.getId());
        faultLog1.setDispatcherId(1L);
        faultLog1.setCabinetId(cabinet.getId());
        faultLog1.setFaultType(FaultLog.FaultType.CODE_2.getValue());
        faultLog1.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog1);

        FaultLog faultLog5 = newFaultLog(agent.getId());
        faultLog5.setDispatcherId(1L);
        faultLog5.setCabinetId(cabinet.getId());
        faultLog5.setFaultType(FaultLog.FaultType.CODE_7.getValue());
        faultLog5.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog5);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setBatteryId(battery.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_6.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);

        battery.setNotElectrifyFaultLogId(faultLog.getId());
        insertBattery(battery);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setExpireTime(new Date());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.EXPIRED.getValue());
        packetPeriodOrder.setPrice(2000);
        packetPeriodOrder.setMoney(1000);
        insertPacketPeriodOrder(packetPeriodOrder);


        FaultLog faultLog2 = newFaultLog(agent.getId());
        faultLog2.setDispatcherId(1L);
        faultLog2.setCabinetId(cabinet.getId());
        faultLog2.setFaultType(FaultLog.FaultType.CODE_5.getValue());
        faultLog2.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog2);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setNotTakeTimeoutFaultLogId(faultLog2.getId());

        FaultLog faultLog4 = newFaultLog(agent.getId());
        faultLog4.setDispatcherId(1L);
        faultLog4.setCabinetId(cabinet.getId());
        faultLog4.setFaultType(FaultLog.FaultType.CODE_4.getValue());
        faultLog4.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog4);

        batteryOrder.setPayTimeoutFaultLogId(faultLog4.getId());
        insertBatteryOrder(batteryOrder);

        Long[] idList = new Long[]{faultLog1.getId(),faultLog5.getId(),faultLog2.getId(),faultLog3.getId(),faultLog4.getId(),faultLog.getId()};

        service.updateStatus(idList, "张三");
        assertNull(jdbcTemplate.queryForMap("select temp_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("temp_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog5.getId()).get("status"));

        assertNull(jdbcTemplate.queryForMap("select offline_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("offline_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog1.getId()).get("status"));

        assertNull(jdbcTemplate.queryForMap("select all_full_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("all_full_fault_log_id"));
        assertEquals(0, jdbcTemplate.queryForMap("select all_full_count from hdg_cabinet where id = ?", cabinet.getId()).get("all_full_count"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog3.getId()).get("status"));

        assertNull(jdbcTemplate.queryForMap("select pay_timeout_fault_log_id from hdg_battery_order where id = ?", batteryOrder.getId()).get("pay_timeout_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog4.getId()).get("status"));

        assertNull(jdbcTemplate.queryForMap("select not_take_timeout_fault_log_id from hdg_battery_order where id = ?", batteryOrder.getId()).get("not_take_timeout_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog2.getId()).get("status"));

        assertNull(jdbcTemplate.queryForMap("select not_electrify_fault_log_id from hdg_battery where id = ?", battery.getId()).get("not_electrify_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));

    }

    /**
     * 测试分柜离线
     */
    @Test
    public void update1() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_2.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);

        service.update(faultLog);
        assertNull(jdbcTemplate.queryForMap("select offline_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("offline_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));
    }

    /**
     * 测试无可用空箱
     */
    @Test
    public void update2() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(1L);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_3.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);

        cabinet.setAllFullCount(8);
        cabinet.setAllFullFaultLogId(faultLog.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        service.update(faultLog);
        assertNull(jdbcTemplate.queryForMap("select all_full_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("all_full_fault_log_id"));
        assertEquals(0, jdbcTemplate.queryForMap("select all_full_count from hdg_cabinet where id = ?", cabinet.getId()).get("all_full_count"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));

    }

    /**
     * 测试还电付款超时
     */
    @Test
    public void update3() {
        Partner partner = newPartner(); insertPartner(partner);
        Agent provinceAgent = newAgent(partner.getId());
        //provinceAgent.setGrade(Agent.Grade.PROVINCE.getValue());
        insertAgent(provinceAgent);

        Agent cityAgent = newAgent(partner.getId());
        cityAgent.setParentId(provinceAgent.getId());
        //cityAgent.setGrade(Agent.Grade.CITY.getValue());
        insertAgent(cityAgent);

        Agent agent = newAgent(partner.getId());
        agent.setParentId(cityAgent.getId());
        //agent.setGrade(Agent.Grade.DISTRICT.getValue());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);





        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);
          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setExpireTime(new Date());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.EXPIRED.getValue());
        packetPeriodOrder.setPrice(2000);
        packetPeriodOrder.setMoney(1000);
        insertPacketPeriodOrder(packetPeriodOrder);


        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_4.getValue());
        insertFaultLog(faultLog);
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setPayTimeoutFaultLogId(faultLog.getId());
        insertBatteryOrder(batteryOrder);

        service.update(faultLog);

        assertNull(jdbcTemplate.queryForMap("select pay_timeout_fault_log_id from hdg_battery_order where id = ?", batteryOrder.getId()).get("pay_timeout_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));

    }

    /**
     * 测试新电未取超时
     */
    @Test
    public void update4() {
        Partner partner = newPartner(); insertPartner(partner);
        Agent provinceAgent = newAgent(partner.getId());
        //provinceAgent.setGrade(Agent.Grade.PROVINCE.getValue());
        insertAgent(provinceAgent);

        Agent cityAgent = newAgent(partner.getId());
        cityAgent.setParentId(provinceAgent.getId());
        //cityAgent.setGrade(Agent.Grade.CITY.getValue());
        insertAgent(cityAgent);

        Agent agent = newAgent(partner.getId());
        agent.setParentId(cityAgent.getId());
        //agent.setGrade(Agent.Grade.DISTRICT.getValue());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);





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
          insertBattery(battery);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setExpireTime(new Date());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.EXPIRED.getValue());
        packetPeriodOrder.setPrice(2000);
        packetPeriodOrder.setMoney(1000);
        insertPacketPeriodOrder(packetPeriodOrder);


        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_5.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setNotTakeTimeoutFaultLogId(faultLog.getId());
        insertBatteryOrder(batteryOrder);

        service.update(faultLog);

        assertNull(jdbcTemplate.queryForMap("select not_take_timeout_fault_log_id from hdg_battery_order where id = ?", batteryOrder.getId()).get("not_take_timeout_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));
    }

    /**
     * 测试电池未连接充电器
     */
    @Test
    public void update5() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setBatteryId(battery.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_6.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);

        battery.setNotElectrifyFaultLogId(faultLog.getId());
        insertBattery(battery);
        service.update(faultLog);
        assertNull(jdbcTemplate.queryForMap("select not_electrify_fault_log_id from hdg_battery where id = ?", battery.getId()).get("not_electrify_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));

    }

    /**
     * 测试分柜温度
     */
    @Test
    public void update6() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_7.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);


        service.update(faultLog);
        assertNull(jdbcTemplate.queryForMap("select temp_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("temp_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));
    }

    @Test
    public void update7(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(),role.getId(),dept.getId());
        insertUser(user);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        cabinet.setDispatcherId(user.getId());
        insertCabinet(cabinet);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_7.getValue());
        insertFaultLog(faultLog);
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());

        assertTrue(service.update(faultLog).isSuccess());
    }

}
