package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class BackBatteryOrderServiceTest extends BaseJunit4Test {
    @Autowired
    BackBatteryOrderService backBatteryOrderService;

    @Test
    public void clearLockTime() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);



        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "0001");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.BACK_LOCK.getValue());
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        BackBatteryOrder order = newBackBatteryOrder(
                "1111111111111",
                agent.getId(),
                cabinet.getId(),
                subcabinetBox.getBoxNum(),
                battery.getId(),
                customer.getId());
        order.setExpireTime(DateUtils.addDays(new Date(), -1));
        insertBackBatteryOrder(order);

        backBatteryOrderService.refreshExpireOrder();

        assertEquals(Battery.Status.CUSTOMER_OUT.getValue(), jdbcTemplate.queryForInt("select status from hdg_battery where id = ? ", battery.getId()));
        assertEquals(BackBatteryOrder.OrderStatus.EXPIRE.getValue(), jdbcTemplate.queryForInt("select order_status from hdg_back_battery_order where id = ?", order.getId()));
        assertEquals(CabinetBox.BoxStatus.EMPTY.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", cabinet.getId(), "0001"));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where back_battery_order_id is null and id = ?", order.getCustomerId()));
    }


}
