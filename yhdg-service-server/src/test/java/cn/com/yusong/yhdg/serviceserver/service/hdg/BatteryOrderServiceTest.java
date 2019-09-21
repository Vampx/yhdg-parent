package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;


public class BatteryOrderServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryOrderService batteryOrderService;


    /**
     * 换电订单取电超时
     */
    @Test
    public void batteryOrderNotTakeTimeOut() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

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

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.INIT.getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -600);
        batteryOrder.setCreateTime(calendar.getTime());
        batteryOrder.setTakeCabinetId(cabinet.getId());
        insertBatteryOrder(batteryOrder);
        batteryOrderService.batteryOrderNotTakeTimeOut();
        assertNotNull(jdbcTemplate.queryForMap("select not_take_timeout_fault_log_id from hdg_battery_order where id = ?", batteryOrder.getId()).get("not_take_timeout_fault_log_id"));
    }

    @Test
    public void moveHistory() {
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

        Date date = new Date();
        //今天单子不计入历史库
        BatteryOrder batteryOrder1 = newBatteryOrder(OrderId.PREFIX_BATTERY + DateFormatUtils.format(date, OrderId.DATE_FORMAT) + "0000000001", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder1.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder1.setCreateTime(date);
        insertBatteryOrder(batteryOrder1);
        //整三个月前不计入历史库
        date = DateUtils.addMonths(date, -3);
        BatteryOrder batteryOrder2 = newBatteryOrder(OrderId.PREFIX_BATTERY + DateFormatUtils.format(date, OrderId.DATE_FORMAT) + "0000000002", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder2.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder2.setCreateTime(date);
        insertBatteryOrder(batteryOrder2);
        //超出三个月计入历史库
        date = DateUtils.addDays(date, -1);
        BatteryOrder batteryOrder3 = newBatteryOrder(OrderId.PREFIX_BATTERY + DateFormatUtils.format(date, OrderId.DATE_FORMAT) + "0000000003", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder3.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder3.setCreateTime(date);
        insertBatteryOrder(batteryOrder3);
        //超出三个月但不为已支付不计入历史库
        BatteryOrder batteryOrder4 = newBatteryOrder(OrderId.PREFIX_BATTERY + DateFormatUtils.format(date, OrderId.DATE_FORMAT) + "0000000004", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder4.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        batteryOrder4.setCreateTime(date);
        insertBatteryOrder(batteryOrder4);
        batteryOrderService.moveHistory();
        assertEquals(3, jdbcTemplate.queryForInt("select COUNT(*) from hdg_battery_order "));
        assertEquals(1, jdbcTemplate.queryForInt("select COUNT(*) from hdg_battery_order where id = ?", batteryOrder1.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select COUNT(*) from hdg_battery_order where id = ?", batteryOrder2.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select COUNT(*) from hdg_battery_order where id = ?", batteryOrder3.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select COUNT(*) from hdg_battery_order where id = ?", batteryOrder4.getId()));

    }
}
