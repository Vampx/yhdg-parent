package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.protocol.msg08.HeartParam;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class CabinetServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetService cabinetService;

    @Test
    public void findMaxId() {
        assertNotNull(cabinetService.findMaxId());
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

        assertNotNull(cabinetService.find(cabinet.getId()));
    }

    /**
     * 测试OfflineFaultLogId
     */
    @Test
    public void heart_0() {
        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 0;
        box.batteryId = "00000000";
        box.volume = 0;
        param.boxList.add(box);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode("000001");
        insertCabinetCode(subcabinetCode);

        FaultLog faultLog = newFaultLog(agent.getId());
        insertFaultLog(faultLog);

        Cabinet subcabinet1 = newCabinet(agent.getId(), null);
        subcabinet1.setOfflineFaultLogId(faultLog.getId());
        subcabinet1.setId("000001");
        insertCabinet(subcabinet1);

        Cabinet subcabinet = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null, null);

        assertEquals(FaultLog.Status.PROCESSED.getValue(), jdbcTemplate.queryForInt("select status from hdg_fault_log where id = ?", faultLog.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet where id = ? and offline_fault_log_id is null", subcabinet.getId()));
    }

    /**
     * 测试不存在的格子的新建
     * 测试格子的打开状态正确
     * 测试格子的open_time正确
     */
    @Test
    public void heart_1() {
        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 0;
        box.batteryId = "00000000";
        box.volume = 0;
        param.boxList.add(box);


        Cabinet subcabinet = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null,null);
        String subcabinetId = subcabinet.getId();

        Cabinet cabinet = new Cabinet();
        cabinet.setId("201701010001");
        cabinet.setAgentId(1);
        cabinet.setCabinetName("201701010001");
        cabinet.setProvinceId(Constant.PROVINCE_ID);
        cabinet.setCityId(Constant.CITY_ID);
        cabinet.setDistrictId(Constant.DISTRICT_ID);
        cabinet.setLng(Constant.LNG);
        cabinet.setLat(Constant.LAT);
        cabinet.setGeoHash(Constant.GEO_HASH);
        cabinet.setActiveStatus(Cabinet.ActiveStatus.ENABLE.getValue());
        cabinet.setWorkTime("00:00 ~ 23:00");
        cabinet.setTel("");
        cabinet.setKeyword("201701010001");
        cabinet.setAllFullCount(0);
        cabinet.setCreateTime(new Date());
        cabinet.setChargeFullVolume(100);
        cabinet.setNetworkType(1);
        cabinet.setPermitExchangeVolume(100);
        cabinet.setChargeFullVolume(100);
        cabinet.setMaxChargePower(90);
        cabinet.setBoxMinPower(10);
        cabinet.setBoxMaxPower(50);
        cabinet.setBoxTrickleTime(12);
        cabinet.setIsFpOpen(ConstEnum.Flag.FALSE.getValue());
        cabinet.setRentPeriodType(0);
        cabinet.setPrice(1.2);
        cabinet.setActiveStatus(ConstEnum.Flag.TRUE.getValue());
        cabinet.setIsOnline(ConstEnum.Flag.FALSE.getValue());
        cabinet.setMaxChargeCount(3);
        cabinet.setSubtype(Cabinet.Subtype.EXCHANGE.getValue());
        cabinet.setFaultType(1);
        cabinet.setNetworkType(Cabinet.NetworkType.NETWORK_0.getValue());
        cabinet.setActiveFanTemp(Constant.ACTIVE_FAN_TEMP);
        cabinet.setChargeFullVolume(Constant.FULL_VOLUME);//满电电量
        cabinet.setPermitExchangeVolume(Constant.PERMIT_EXCHANGE_VOLUME);//可换电电量
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.NOT_ONLINE.getValue());//未上线
        cabinet.setForegiftMoney(ConstEnum.Flag.FALSE.getValue());
        cabinet.setRentMoney(ConstEnum.Flag.FALSE.getValue());
        insertCabinet(cabinet);

        cabinetService.heart(param, subcabinetId, null, null);
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetId, "01"));
        assertEquals(1, jdbcTemplate.queryForInt("select is_open from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetId, "01"));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and open_time is not null", subcabinetId, "01"));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_online_stats"));
    }

    /**
     * 测试不存在的格子的新建
     */
    @Test
    public void heart_2() {
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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        insertCabinetBox(subcabinetBox);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        insertBattery(battery);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 0;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null,null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetId, "01"));
    }

    /**
     * 测试未使用的电池放到格子中
     */
    @Test
    public void heart_3() {
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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.NOT_USE.getValue());
        insertBattery(battery);


        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null,null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(Battery.Status.IN_BOX.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery where in_box_time is not null and cabinet_id = ? and cabinet_name is not null and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));
    }

    /**
     * 测试客户使用中的电池放到格子中(无包月套餐)
     */
    @Test
    public void heart_4() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setCustomerId(customer.getId());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery);


        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = battery.getId();
        box.volume = 0;
        param.boxList.add(box);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        cabinet = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null,null);
        String subcabinetId = cabinet.getId();

        List<Integer> openBoxList = new ArrayList<Integer>();

        cabinetService.heart(param, subcabinetId, openBoxList, null);

        assertEquals(Battery.Status.IN_BOX_NOT_PAY.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder.getId()));
        assertEquals(CabinetBox.BoxStatus.CUSTOMER_USE.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery_order where id = ? and error_message is not null", batteryOrder.getId()));
    }

    /**
     * 测试客户使用中的电池放到格子中(有包月套餐)
     */
    @Test
    public void heart_4_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        //格口1
        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setCustomerId(customer.getId());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery);

        //电池2
        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("000000000012");
        battery2.setCustomerId(customer.getId());
        battery2.setStatus(Battery.Status.IN_BOX.getValue());
        battery2.setVolume(100);
        battery2.setUpLineStatus(1);
        insertBattery(battery2);

        //格口2
        CabinetBox subcabinetBox2 = newCabinetBox(cabinet.getId(), "2");
        subcabinetBox2.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        subcabinetBox2.setBatteryId(battery2.getId());
        subcabinetBox2.setIsActive(1);
        subcabinetBox2.setIsOnline(1);
        insertCabinetBox(subcabinetBox2);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = battery.getId();
        box.volume = 0;
        param.boxList.add(box);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null,null).getId();

        List<Integer> openBoxList = new ArrayList<Integer>();

        cabinetService.heart(param, subcabinetId, openBoxList, null);

        assertEquals(Battery.Status.IN_BOX.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(BatteryOrder.OrderStatus.PAY.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder.getId()));
        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where id = ?", customer.getId()));

        assertEquals(PacketPeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrder.getId()));
    }

    /**
     * 测试维护取出的电池放到格子中
     */
    @Test
    public void heart_5() {
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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.KEEPER_OUT.getValue());
        insertBattery(battery);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        KeepOrder keepOrder = newKeepOrder(newOrderId(OrderId.OrderIdType.KEEP_ORDER), agent.getId(), battery.getId());
        keepOrder.setOrderStatus(KeepOrder.OrderStatus.OUT.getValue());
        insertKeepOrder(keepOrder);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", keepOrder.getId(), battery.getId());

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null,null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(Battery.Status.IN_BOX.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(KeepOrder.OrderStatus.IN.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_keep_order where id = ?", keepOrder.getId()));
        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery where id = ? and order_id is null", battery.getId()));
    }

    /**
     * 测试重开箱门 客户拿走了未付款电池
     */
    @Test
    public void heart_6() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.IN_BOX_NOT_PAY.getValue());
        insertBattery(battery);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 0;
        box.batteryId = null;
        box.volume = 0;
        param.boxList.add(box);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        insertBatteryOrder(batteryOrder);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null,null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(Battery.Status.CUSTOMER_OUT.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(BatteryOrder.OrderStatus.TAKE_OUT.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder.getId()));
        assertEquals(CabinetBox.BoxStatus.EMPTY.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id is null", cabinet.getId(), subcabinetBox.getBoxNum()));
    }

    /**
     * 测试维护取出了电池
     */
    @Test
    public void heart_7() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        User user = newUser(agent.getId(), null, null);
        insertUser(user);


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
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setOpenType(CabinetBox.OpenType.KEEP_ORDER_OPEN_FULL_BOX.getValue());
        subcabinetBox.setOpenerId(user.getId());
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 0;
        box.batteryId = null;
        box.volume = 0;
        param.boxList.add(box);

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(Battery.Status.KEEPER_OUT.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(KeepOrder.OrderStatus.OUT.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_keep_order where battery_id = ?", battery.getId()));
        assertEquals(CabinetBox.BoxStatus.EMPTY.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id is null", cabinet.getId(), subcabinetBox.getBoxNum()));
    }

    /**
     * 测试客户取出了新电池
     */
    @Test
    public void heart_8() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
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

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.IN_BOX_CUSTOMER_USE.getValue());
        battery.setOrderId(orderId);
        insertBattery(battery);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        BatteryOrder batteryOrder = newBatteryOrder(orderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        battery.setStatus(BatteryOrder.OrderStatus.INIT.getValue());
        insertBatteryOrder(batteryOrder);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 0;
        box.batteryId = null;
        box.volume = 0;
        param.boxList.add(box);

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(Battery.Status.CUSTOMER_OUT.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(BatteryOrder.OrderStatus.TAKE_OUT.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where battery_id = ?", battery.getId()));
        assertEquals(CabinetBox.BoxStatus.EMPTY.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id is null", cabinet.getId(), subcabinetBox.getBoxNum()));
    }

    /**
     * 测试客户使用中的电池退租
     */
    @Test
    public void heart_9() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.BACK_LOCK.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setCustomerId(customer.getId());
        insertBattery(battery);


        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        BackBatteryOrder backOrder = newBackBatteryOrder(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER), agent.getId(),
                cabinet.getId(), "01", battery.getId(), customer.getId());
        insertBackBatteryOrder(backOrder);
        jdbcTemplate.update("update bas_customer set back_battery_order_id = ?, battery_id = ? where id = ?", backOrder.getId(), battery.getId(), customer.getId());

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(Battery.Status.IN_BOX.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(BatteryOrder.OrderStatus.PAY.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder.getId()));
        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where id = ? and back_battery_order_id is null", customer.getId()));
    }

    /**
     * 测试客户使用中的电池退租(电池不一致  原电池绑定用户)
     */
    @Test
    public void heart_9_1() {
        Partner partner = newPartner();
        insertPartner(partner);

        //客户1
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        //客户2
        Customer customer2 = newCustomer(partner.getId());
        customer2.setMobile("12345676567");
        insertCustomer(customer2);


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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.BACK_LOCK.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setCustomerId(customer.getId());
        insertBattery(battery);


        //电池2
        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("000000000012");
        battery2.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery2.setCustomerId(customer2.getId());
        battery2.setCode("12131");
        insertBattery(battery2);

        //电池3
        Battery battery3 = newBattery(agent.getId(), systemBatteryType.getId());
        battery3.setId("000000000013");
        battery3.setStatus(Battery.Status.NOT_USE.getValue());
        battery3.setCode("12132");
        insertBattery(battery3);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000012";
        box.volume = 0;
        param.boxList.add(box);

        //订单1
        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        //订单2
        BatteryOrder batteryOrder2 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery2.getId(), customer2.getId());
        batteryOrder2.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder2);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());
        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder2.getId(), battery2.getId());

        BackBatteryOrder backOrder = newBackBatteryOrder(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER), agent.getId(),
                cabinet.getId(), "01", battery.getId(), customer.getId());
        insertBackBatteryOrder(backOrder);
        jdbcTemplate.update("update bas_customer set back_battery_order_id = ?, battery_id = ? where id = ?", backOrder.getId(), battery.getId(), customer.getId());

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from hdg_back_battery_order where cabinet_id = ? and box_num = ? and order_status = ?", cabinet.getId(), "01", 1));

        List<Integer> openBoxList = new ArrayList<Integer>();

        cabinetService.heart(param, subcabinetId, openBoxList, null);


        assertEquals(Battery.Status.CUSTOMER_OUT.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));

        assertEquals(Battery.Status.IN_BOX_NOT_PAY.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery2.getId()));

        assertEquals(BatteryOrder.OrderStatus.TAKE_OUT.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder.getId()));

        assertEquals(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder2.getId()));

        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where id = ? and back_battery_order_id is null", customer.getId()));
    }

    /**
     * 测试客户使用中的电池退租(电池不一致  原电池为空)
     */
    @Test
    public void heart_9_2() {
        Partner partner = newPartner();
        insertPartner(partner);

        //客户1
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        //客户2
        Customer customer2 = newCustomer(partner.getId());
        customer2.setMobile("12345676567");
        insertCustomer(customer2);

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.BACK_LOCK.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setCustomerId(customer.getId());
        insertBattery(battery);


        //电池3
        Battery battery3 = newBattery(agent.getId(), systemBatteryType.getId());
        battery3.setId("000000000013");
        battery3.setStatus(Battery.Status.NOT_USE.getValue());
        battery3.setCode("12132");
        insertBattery(battery3);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000013";
        box.volume = 0;
        param.boxList.add(box);

        //订单1
        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);


        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        BackBatteryOrder backOrder = newBackBatteryOrder(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER), agent.getId(),
                cabinet.getId(), "01", battery.getId(), customer.getId());
        insertBackBatteryOrder(backOrder);
        jdbcTemplate.update("update bas_customer set back_battery_order_id = ?, battery_id = ? where id = ?", backOrder.getId(), battery.getId(), customer.getId());

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from hdg_back_battery_order where cabinet_id = ? and box_num = ? and order_status = ?", cabinet.getId(), "01", 1));

        List<Integer> openBoxList = new ArrayList<Integer>();

        cabinetService.heart(param, subcabinetId, openBoxList, null);


        assertEquals(Battery.Status.CUSTOMER_OUT.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));

        assertEquals(Battery.Status.IN_BOX.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery3.getId()));

        assertEquals(BatteryOrder.OrderStatus.TAKE_OUT.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder.getId()));

        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where id = ? and back_battery_order_id is null", customer.getId()));
    }

    /**
     * 测试放入柜子后设置停止充电
     */
    @Test
    public void heart_10() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "01");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.BACK_LOCK.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setCustomerId(customer.getId());
        insertBattery(battery);


        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        BatteryChargeRecord chargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(chargeRecord);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        BackBatteryOrder backOrder = newBackBatteryOrder(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER), agent.getId(),
                cabinet.getId(), "01", battery.getId(), customer.getId());
        insertBackBatteryOrder(backOrder);
        jdbcTemplate.update("update bas_customer set back_battery_order_id = ?, battery_id = ? where id = ?", backOrder.getId(), battery.getId(), customer.getId());

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(Battery.Status.IN_BOX.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(BatteryOrder.OrderStatus.PAY.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_battery_order where id = ?", batteryOrder.getId()));
        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), box.batteryId));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where id = ? and back_battery_order_id is null", customer.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery where id = ? and charge_record_id is null and charge_status = ?", battery.getId(), Battery.ChargeStatus.NOT_CHARGE.getValue()));
    }

    /**
     * 测试不关门 直接换个电池
     */
    @Test
    public void heart_11() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setCode("000000000011");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        insertBattery(battery);


        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("000000000012");
        battery2.setCode("000000000012");
        battery2.setStatus(Battery.Status.NOT_USE.getValue());
        insertBattery(battery2);

        jdbcTemplate.update("update hdg_cabinet_box set battery_id = ? where cabinet_id = ? and box_num = ?", battery.getId(), cabinet.getId(), subcabinetBox.getBoxNum());


        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000012";
        box.volume = 0;
        param.boxList.add(box);

        cabinetService.heart(param, cabinet.getId(), null, null);

        assertEquals(Battery.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery.getId()));
        assertEquals(Battery.Status.IN_BOX.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ?", battery2.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ? and battery_id = ?", cabinet.getId(), subcabinetBox.getBoxNum(), battery2.getId()));

    }

    /**
     * 测试新版心跳上传未注册电池
     */
    @Test
    public void heart_12() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("100816");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        HeartParam param = new HeartParam();
        param.code = "100816";
        param.version = "1.0";
        param.degree = 100;
        param.network = 0;
        param.signal = 30;
        param.fireState = 0;
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryCode = "10000011";
        box.voltage = 60;
        box.electricity = 22;
        box.volume = 80;
        box.protectState = 2;
        box.fet = 1;
        box.chargeStatus = 0;
        box.power = 100;
        param.boxList.add(box);

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        cabinetService.heart(param, subcabinetId, null, null);


        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery where code = ?", box.batteryCode));

    }

    /**
     * 测试新版心跳上传未注册电池并且开始充电
     */
    @Test
    public void heart_13() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("100816");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("10000011");
        battery.setCode("10000011");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        insertBattery(battery);


        HeartParam param = new HeartParam();
        param.code = "100816";
        param.version = "1.0";
        param.degree = 100;
        param.network = 0;
        param.signal = 30;
        param.fireState = 0;
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryCode = "10000011";
        box.voltage = 60;
        box.electricity = 22;
        box.volume = 80;
        box.protectState = 2;
        box.fet = 1;
        box.chargeStatus = 1;
        box.power = 100;
        param.boxList.add(box);

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery where code = ?", box.batteryCode));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery_charge_record where battery_id = ?", battery.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery_charge_record where battery_id = ?", battery.getId()));
    }

    /**
     * 测试新版心跳上传未注册电池并且结束充电
     */
    @Test
    public void heart_14() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("100816");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("10000011");
        battery.setCode("10000011");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        insertBattery(battery);


        HeartParam param = new HeartParam();
        param.code = "100816";
        param.version = "1.0";
        param.degree = 100;
        param.network = 0;
        param.signal = 30;
        param.fireState = 0;
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryCode = "10000011";
        box.voltage = 60;
        box.electricity = 22;
        box.volume = 80;
        box.protectState = 2;
        box.fet = 1;
        box.chargeStatus = 1;
        box.power = 100;
        param.boxList.add(box);

        String subcabinetId = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        cabinetService.heart(param, subcabinetId, null, null);

        HeartParam param2 = new HeartParam();
        param2.code = "100816";
        param2.version = "1.0";
        param2.degree = 100;
        param2.network = 0;
        param2.signal = 30;
        param2.fireState = 0;
        param2.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box2 = new HeartParam.Box();
        box2.boxNum = 1;
        box2.isClosed = 1;
        box2.batteryCode = "10000011";
        box2.voltage = 60;
        box2.electricity = 22;
        box2.volume = 80;
        box2.protectState = 2;
        box2.fet = 1;
        box2.chargeStatus = 0;
        box2.power = 100;
        param2.boxList.add(box2);

        cabinetService.heart(param2, subcabinetId, null, null);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery where code = ?", box2.batteryCode));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery_charge_record where battery_id = ?", battery.getId()));
        System.out.println(jdbcTemplate.queryForMap("select end_time as endTime from hdg_battery_charge_record where battery_id = ?", battery.getId()).get("endTime"));
        assertNotNull(jdbcTemplate.queryForMap("select end_time as endTime from hdg_battery_charge_record where battery_id = ?", battery.getId()).get("endTime"));
    }


    /**
     * 测试用电量
     */
    @Test
    public void heart_15() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("100816");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("10000011");
        battery.setCode("10000011");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        insertBattery(battery);


        String subcabinetId = cabinetService.insertOrUpdate("10086", "1.0", 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null, null, null).getId();
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_day_degree_stats where cabinet_id = ?", subcabinetId));

    }

    /**
     * 测试负数用电量
     */
    @Test
    public void heart_16() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("100816");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("10000011");
        battery.setCode("10000011");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        insertBattery(battery);


        String subcabinetId = cabinetService.insertOrUpdate("100816", "1.0", 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, -1, null, null, null, null, null).getId();
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_day_degree_stats where cabinet_id = ?", subcabinetId));

    }

    /**
     * 测试更新用电量
     */
    @Test
    public void heart_17() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("100816");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("10000011");
        battery.setCode("10000011");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        insertBattery(battery);


        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), DateFormatUtils.format(new Date(), Constant.DATE_FORMAT));
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);
        String subcabinetId = cabinetService.insertOrUpdate("100816", "1.0", 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 200, null, null, null, null, null).getId();
        assertEquals(200, jdbcTemplate.queryForInt("select num from hdg_cabinet_day_degree_stats where cabinet_id = ?", subcabinetId));

    }

    /**
     * 测试小于开始用电量
     */
    @Test
    public void heart_18() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("100816");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("10000011");
        battery.setCode("10000011");
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        insertBattery(battery);


        CabinetDayDegreeStats subcabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), DateFormatUtils.format(new Date(), Constant.DATE_FORMAT));
        subcabinetDayDegreeStats.setBeginNum(20);
        subcabinetDayDegreeStats.setEndNum(120);
        insertCabinetDayDegreeStats(subcabinetDayDegreeStats);
        String subcabinetId = cabinetService.insertOrUpdate("100816", "1.0", 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 19, null, null, null, null, null).getId();
        assertEquals(0, jdbcTemplate.queryForInt("select num from hdg_cabinet_day_degree_stats where cabinet_id = ?", subcabinetId));

    }


    /**
     * 测试箱门关闭状态下 客户使用未取出的电池 要重新开箱
     * 收集的箱号=列表箱号
     */
    @Test
    public void heart_19() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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
        //电池1
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setCode("10000011");
        battery.setStatus(Battery.Status.IN_BOX_CUSTOMER_USE.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        insertBattery(battery);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        List<Integer> openBoxList = new ArrayList<Integer>();

        cabinetService.heart(param, cabinet.getId(), openBoxList, null);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));

        assertTrue(openBoxList.get(0) == box.boxNum);
    }

    /**
     * 电池当前电量>柜子可换进电量 放电量不满足最低换电电量要重新开箱 发送开箱指令
     * 收集的箱号=列表箱号
     */
    @Test
    public void heart_20() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setPermitExchangeVolume(Constant.PERMIT_EXCHANGE_VOLUME);
        insertCabinet(cabinet);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        insertCabinetBox(subcabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setCustomerId(customer.getId());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setVolume(60);
        insertBattery(battery);


        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        cabinet = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null, null);
        String subcabinetId = cabinet.getId();

        List<Integer> openBoxList = new ArrayList<Integer>();

        cabinetService.heart(param, subcabinetId, openBoxList, null);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));

        assertTrue(openBoxList.get(0) == box.boxNum);

    }

    /**
     * 没有满电电池在格口中(正常流程不扫码 有打开的格口客户直接放进去)要重新开箱
     * 收集的箱号=列表箱号
     */
    @Test
    public void heart_21() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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


        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("000000000011");
        battery.setCustomerId(customer.getId());
        battery.setStatus(Battery.Status.IN_BOX_NOT_PAY.getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 50);
        battery.setInBoxTime(calendar.getTime());
        insertBattery(battery);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        HeartParam param = new HeartParam();
        param.code = "10086";
        param.version = "1.0";
        param.boxList = new ArrayList<HeartParam.Box>();

        HeartParam.Box box = new HeartParam.Box();
        box.boxNum = 1;
        box.isClosed = 1;
        box.batteryId = "000000000011";
        box.volume = 0;
        param.boxList.add(box);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        jdbcTemplate.update("update hdg_battery set order_id  = ? where id = ?", batteryOrder.getId(), battery.getId());

        cabinet = cabinetService.insertOrUpdate(param.code, param.version, 1, 1, 0, 0, Collections.<HeartParam.Box>emptyList(), 0, 100, null, null, null,null, null);
        String subcabinetId = cabinet.getId();

        List<Integer> openBoxList = new ArrayList<Integer>();

        cabinetService.heart(param, subcabinetId, openBoxList, null);

        assertEquals(CabinetBox.BoxStatus.EMPTY.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), subcabinetBox.getBoxNum()));

        assertTrue(openBoxList.get(0) == box.boxNum);

    }

    @Test
    public void handleLaxinCustomerByMonth() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setIncomeType(Laxin.IncomeType.MONTH.getValue());
        laxin.setPacketPeriodMoney(100);
        laxin.setPacketPeriodMonth(10);
        insertLaxin(laxin);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile(customer.getMobile());
        laxinCustomer.setForegiftTime(new Date());
        laxinCustomer.setPacketPeriodMoney(100);
        laxinCustomer.setPacketPeriodMonth(10);
        laxinCustomer.setIncomeType(Laxin.IncomeType.MONTH.getValue());
        laxinCustomer.setPacketPeriodExpireTime(DateUtils.addDays(new Date(), 1));
        insertLaxinCustomer(laxinCustomer);

        cabinetService.handleLaxinCustomerByMonth(agent, customer, 1);

        assertEquals(100, jdbcTemplate.queryForInt("select laxin_money from bas_laxin_record where laxin_id = ? and target_customer_id = ?", laxin.getId(), customer.getId()));
    }

    /*测试按次*/
    @Test
    public void handleLaxinCustomerByMonth_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setIncomeType(Laxin.IncomeType.MONTH.getValue());
        laxin.setPacketPeriodMoney(100);
        laxin.setPacketPeriodMonth(10);
        insertLaxin(laxin);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile(customer.getMobile());
        laxinCustomer.setForegiftTime(new Date());
        laxinCustomer.setPacketPeriodMoney(null);
        laxinCustomer.setPacketPeriodMonth(null);
        laxinCustomer.setLaxinMoney(100);
        laxinCustomer.setIncomeType(Laxin.IncomeType.TIMES.getValue());
        laxinCustomer.setPacketPeriodExpireTime(DateUtils.addDays(new Date(), 1));
        insertLaxinCustomer(laxinCustomer);

        cabinetService.handleLaxinCustomerByMonth(agent, customer, 1);

        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_laxin_record where laxin_id = ? and target_customer_id = ?", laxin.getId(), customer.getId()));
    }
}
