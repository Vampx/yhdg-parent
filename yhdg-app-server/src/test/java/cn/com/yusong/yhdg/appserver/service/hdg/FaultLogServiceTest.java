package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/11.
 */
public class FaultLogServiceTest extends BaseJunit4Test {

    @Autowired
    FaultLogService faultLogService;

    @Test
    public void find() {
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

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);

        /**
         * insert into hdg_fault_log
         * (fault_level,dispatcher_id,agent_id,battery_id,cabinet_id,cabinet_name,
         *  cabinet_address,box_num,fault_type,fault_content,
         *  status,handle_time,handler_name,create_time)
         *  values
         *  (2,2,1,'00001','001','001','001','001','七链路','01',1,'防盗报警'
         *  ,1,sysdate(),'',sysdate())
         */
        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        int flag = faultLogService.findCountByDispatcher(1, 1);
        System.out.println("flag==" + flag);

        assertEquals(1, flag);

    }

    @Test
    public void findCabenitCount() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);


        // User user = newUser(agent.getId(), role.getId(), dept.getId());
        User user = new User();
        user.setCreateTime(new Date());
        user.setFullname("");
        user.setIsActive(1);
        user.setIsAdmin(1);
        user.setLoginName("test");
        user.setPassword(CodecUtils.password("123456"));
        user.setMobile("123123123");
        user.setIsProtected(1);
        user.setAccountType(User.AccountType.AGENT.getValue());

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

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        Map<String, Integer> map = faultLogService.findCabenitCount(user.getId(),
                cabinet.getId(), FaultLog.Status.WAIT_PROCESS.getValue());
        assertEquals((Integer) 1, map.get("faultCount"));
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

        User user = new User();
        user.setCreateTime(new Date());
        user.setFullname("");
        user.setIsActive(1);
        user.setIsAdmin(1);
        user.setLoginName("test");
        user.setPassword(CodecUtils.password("123456"));
        user.setMobile("123123123");
        user.setIsProtected(1);
        user.setAccountType(User.AccountType.AGENT.getValue());

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
//
        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        List<FaultLog> faultLogList = faultLogService.findList(user.getId(),
                FaultLog.Status.WAIT_PROCESS.getValue(), 0, 10);
        assertNotNull(faultLogList);
    }

    @Test
    public void updateHandle() throws ParseException {
        Date date = new SimpleDateFormat(Constant.DATE_TIME_FORMAT).parse("2017-01-01 00:01:01");

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

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        assertEquals(1, faultLogService.handle(faultLog.getId(), FaultLog.HandleType.SYSTEM.getValue(), "handlememo", user.getFullname(), date));

        List<FaultLog> faultLogList = faultLogService.findList(user.getId(),
                FaultLog.Status.PROCESSED.getValue(), 0, 10);
        assertNotNull(faultLogList);

    }

    /**
     * 测试换电柜离线
     */
    @Test
    public void handle1() {
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
        faultLog.setFaultType(FaultLog.FaultType.CODE_2.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);

        faultLogService.handle(faultLog.getId(), FaultLog.HandleType.MANUAL.getValue(), "", "", new Date());
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));
    }

    /**
     * 测试无可用空箱
     */
    @Test
    public void handle2() {
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
        cabinet.setDispatcherId(user.getId());
        cabinet.setAllFullFaultLogId(faultLog.getId());

        insertCabinet(cabinet);

        faultLogService.handle(faultLog.getId(), FaultLog.HandleType.MANUAL.getValue(), "", "", new Date());
        assertNull(jdbcTemplate.queryForMap("select all_full_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("all_full_fault_log_id"));
        assertEquals(0, jdbcTemplate.queryForMap("select all_full_count from hdg_cabinet where id = ?", cabinet.getId()).get("all_full_count"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));

    }

    /**
     * 测试还电付款超时
     */
    @Test
    public void handle3() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent provinceAgent = newAgent(partner.getId());
        //provinceAgent.setGrade(Agent.Grade.PROVINCE.getValue());
        insertAgent(provinceAgent);

        Agent cityAgent = newAgent(partner.getId());
        cityAgent.setParentId(provinceAgent.getId());
        insertAgent(cityAgent);

        Agent agent = newAgent(partner.getId());
        agent.setParentId(cityAgent.getId());
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
        faultLog.setFaultType(FaultLog.FaultType.CODE_4.getValue());
        insertFaultLog(faultLog);
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPayTimeoutFaultLogId(faultLog.getId());
        insertBatteryOrder(batteryOrder);

        faultLogService.handle(faultLog.getId(), FaultLog.HandleType.MANUAL.getValue(), "", "", new Date());

        assertNull(jdbcTemplate.queryForMap("select pay_timeout_fault_log_id from hdg_battery_order where id = ?", batteryOrder.getId()).get("pay_timeout_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));

    }

    /**
     * 测试新电未取超时
     */
    @Test
    public void handle4() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent provinceAgent = newAgent(partner.getId());
        insertAgent(provinceAgent);

        Agent cityAgent = newAgent(partner.getId());
        cityAgent.setParentId(provinceAgent.getId());
        insertAgent(cityAgent);

        Agent agent = newAgent(partner.getId());
        agent.setParentId(cityAgent.getId());
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

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setNotTakeTimeoutFaultLogId(faultLog.getId());
        insertBatteryOrder(batteryOrder);

        faultLogService.handle(faultLog.getId(), FaultLog.HandleType.MANUAL.getValue(), "", "", new Date());

        assertNull(jdbcTemplate.queryForMap("select not_take_timeout_fault_log_id from hdg_battery_order where id = ?", batteryOrder.getId()).get("not_take_timeout_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));
    }

    /**
     * 测试电池未连接充电器
     */
    @Test
    public void handle5() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setDispatcherId(1L);
        faultLog.setBatteryId(battery.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_6.getValue());
        faultLog.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        insertFaultLog(faultLog);

        battery.setNotElectrifyFaultLogId(faultLog.getId());
        jdbcTemplate.update("update hdg_battery set not_electrify_fault_log_id = ? where id = ?", faultLog.getId(), battery.getId());

        faultLogService.handle(faultLog.getId(), FaultLog.HandleType.MANUAL.getValue(), "", "", new Date());
        assertNull(jdbcTemplate.queryForMap("select not_electrify_fault_log_id from hdg_battery where id = ?", battery.getId()).get("not_electrify_fault_log_id"));
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));

    }

    /**
     * 测试换电柜温度
     */
    @Test
    public void handle6() {
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

        faultLogService.handle(faultLog.getId(), FaultLog.HandleType.MANUAL.getValue(), "", "", new Date());
        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForMap("select status from hdg_fault_log where id = ?", faultLog.getId()).get("status"));
    }

    @Test
    public void updateHandle6() throws ParseException {
        Date date = new SimpleDateFormat(Constant.DATE_TIME_FORMAT).parse("2017-01-01 00:01:01");

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

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setCabinetId(cabinet.getId());
        faultLog.setFaultType(FaultLog.FaultType.CODE_7.getValue());
        insertFaultLog(faultLog);

        assertEquals(1, faultLogService.handle(faultLog.getId(), FaultLog.HandleType.SYSTEM.getValue(), "handlememo", user.getFullname(), date));

        List<FaultLog> faultLogList = faultLogService.findList(user.getId(),
                FaultLog.Status.PROCESSED.getValue(), 0, 10);
        assertNotNull(faultLogList);

    }
}
