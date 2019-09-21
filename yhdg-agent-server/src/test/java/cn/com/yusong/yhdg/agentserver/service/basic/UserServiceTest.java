package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class UserServiceTest extends BaseJunit4Test {

    @Autowired
    UserService userService;

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

        assertNotNull("recode is not exit", userService.find(user.getId()));
    }

    @Test
    public void findByAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertTrue("recode is not exit", 1 == userService.findByAgent(agent.getId()).size());
    }

    @Test
    public void findUnique() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        String username = "cp";
        assertTrue("recode is exit", userService.findUnique(user.getId(), username));
    }

    @Test
    public void findByLoginInfo() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);
        String password = CodecUtils.password("admin");
        assertNotNull("recode is null", userService.findByLoginInfo(user.getLoginName(), "123456"));
    }

    @Test
    public void updateLoginTime() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertTrue("update fail", 1 == userService.updateLoginTime(user.getId()));
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

        assertTrue(1 == userService.findPage(user).getResult().size());
    }

    @Test
    public void create() throws IOException {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());

        assertTrue(userService.create(user).isSuccess());
    }

    @Test
    public void update() throws IOException {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertTrue(userService.update(user).isSuccess());
    }

    @Test
    public void updatePassword() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertTrue(1 == userService.updatePassword(user.getId(), user.getPassword(), "sfsd"));
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertTrue(1 == userService.delete(user.getId()));
    }

    @Test
    public void addPrecondition() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        role.setAgentId(null);
        insertRole(role);

        Dept dept = newDept(agent.getId());
        role.setAgentId(null);
        insertDept(dept);

        assertTrue(userService.addPrecondition().isSuccess());
    }
}
