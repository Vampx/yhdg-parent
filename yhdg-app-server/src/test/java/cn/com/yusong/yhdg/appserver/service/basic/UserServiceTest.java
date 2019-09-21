package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/9.
 */
public class UserServiceTest extends BaseJunit4Test {

    @Autowired
    UserService userService;

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

        User user1 = userService.find(user.getId());
        assertNotNull(user1);
        User user2 = userService.findByLoginName(user.getLoginName());
        assertNotNull(user2);

        int flag = userService.updatePassword(user.getId(), user.getPassword(), "147852369");
        assertEquals(1, flag);

    }

    @Test
    public void findByLoginName() {
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

        assertNotNull(userService.findByLoginName(user.getLoginName()));
    }

    @Test
    public void findByMobile() {
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

        assertNotNull(userService.findByMobile(user.getMobile()));
    }

    @Test
    public void updatePassword() {
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

        assertEquals(1, userService.updatePassword(user.getId(), CodecUtils.password(Constant.DEFAULT_PASSWORD), CodecUtils.password("123456")));
    }

    @Test
    public void updatePassword2() {
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

        assertEquals(1, userService.updatePassword2(user.getId(), CodecUtils.password("123456")));
    }

    @Test
    public void updateMobile() {
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

        assertEquals(1, userService.updateMobile(user.getId(), user.getMobile()));
    }

    @Test
    public void updateLoginTime() {
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

        assertEquals(1, userService.updateLoginTime(user.getId(), new Date()));
    }

    @Test
    public void updateInfo() {
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

        assertEquals(1, userService.updateInfo(user.getId(), user.getPhotoPath()));
    }

    @Test
    public void getIndex() {
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


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);


        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);


        FaultLog faultLog1 = newFaultLog(agent.getId());
        faultLog1.setCabinetId(cabinet1.getId());
        insertFaultLog(faultLog1);

//
        RestResult restResult = userService.getIndex(user.getId());
        Map map = (Map) restResult.getData();
        assertEquals(0, restResult.getCode());
    }

    @Test
    public void updatePushToken() {
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

        RestResult restResult = userService.updatePushToken(user.getId(), user.getPushType(), user.getPushToken());
        assertEquals(0, restResult.getCode());
    }
}
