package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceTest extends BaseJunit4Test {

    @Autowired
    RoleService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        assertNotNull("role is null", service.find(role.getId()));
    }

    @Test
    public void findByAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        assertTrue("false", 1 == service.findByAgent(agent.getId()).size());
    }
    @Test
    public void findAgentPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        assertTrue(1 == service.findAgentPage(role).getTotalItems());
        assertTrue(1 == service.findAgentPage(role).getResult().size());
    }
    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        assertTrue("role.size != 1", 1 == service.findPage(role).getResult().size());
    }

    @Test
    public void loadOperCode() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Menu menu = newMenu(null);
        insertMenu(menu);

        Perm perm = newPerm(menu.getId());
        insertPerm(perm);

        Role role = newRole(agent.getId());
        insertRole(role);

        RolePerm rolePerm = newRolePerm(role.getId(), perm.getId());
        insertRolePerm(rolePerm);

        service.loadOperCode(role.getId());
    }

    @Test
    public void create() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());

        assertTrue(service.create(role).isSuccess());
    }

    @Test
    public void update() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        assertTrue(service.update(role).isSuccess());
    }

    @Test
    public void delete1() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);
        assertTrue(service.delete(role.getId()).isSuccess());
    }

    @Test
    public void delete2() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertEquals("delete fail", String.format("角色被用户[%s]使用，删除失败", user.getLoginName()), service.delete(role.getId()).getMessage());
    }

    @Test
    public void batchDelete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        int[] id = new int[1];
        id[0] = role.getId();

        assertEquals("batchDelete fail", String.format("成功删除%d条记录", 0), service.batchDelete(id).getMessage());
    }

    @Test
    public void batchDelete2() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        Role role1 = newRole(agent.getId());
        insertRole(role1);

        int[] id = new int[1];
        id[0] = role1.getId();

        assertEquals("batchDelete fail", String.format("成功删除%d条记录", 1), service.batchDelete(id).getMessage());
    }




}
