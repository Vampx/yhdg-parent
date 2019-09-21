package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
public class KeepTakeOrderServiceTest extends BaseJunit4Test {
    @Autowired
    KeepTakeOrderService keepTakeOrderService;

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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String cabinetId; /*取电柜子id*/
        String cabinetName;/*取电柜子名称*/
        Integer orderCount; /*数量*/
        /**
         *   create_time datetime not null,
         id char(20) not null, /*cabinet_id + yyyyMMdd 例如: T00000120170101*/
//        agent_id int unsigned not null,
//                cabinet_id char(6), /*取电柜子id*/
//                cabinet_name char(6), /*取电柜子名称*/
//                order_count int unsigned not null, /*数量*/
//                last_time datetime not null,
//                create_time datetime not null,


//          insert into hdg_keep_take_order ( id,agent_id,cabinet_id,cabinet_name,order_count,last_time,create_time)
//          values("T000001201701127",1,"001","001",1,sysdate(),sysdate());

/**
 * 00001
 * insert into  hdg_keep_order
 * (id,agent_id,battery_id,take_order_id,take_cabinet_id,take_cabinet_name,
 * take_box_num,take_time,init_volume,take_user_id,take_user_fullname,take_user_mobile,order_status,create_time)
 *  values ("KO123456786431231",1,"00001","T000001201701127","001","001","001","001"
 * ,"01",sysdate(),10,2,"光哥",null,2,sysdate());
 *
 */

        KeepTakeOrder keepTakeOrder = newKeepTakeOrder(cabinet.getId(), cabinet.getCabinetName(), agent.getId());
        insertKeepTakeOrder(keepTakeOrder);
        List<KeepTakeOrder> list = keepTakeOrderService.findList(user.getId(), 0, 10);
        assertNotNull(list);
    }

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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String cabinetId; /*取电柜子id*/
        String cabinetName;/*取电柜子名称*/
        Integer orderCount; /*数量*/

        KeepTakeOrder keepTakeOrder = newKeepTakeOrder(cabinet.getId(), cabinet.getCabinetName(), agent.getId());
        insertKeepTakeOrder(keepTakeOrder);
        assertNotNull(keepTakeOrderService.find(keepTakeOrder.getId()));

    }

    @Test
    public void getList() {
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

        keepTakeOrderService.getList(user.getId(), 0, 10);

    }
}
