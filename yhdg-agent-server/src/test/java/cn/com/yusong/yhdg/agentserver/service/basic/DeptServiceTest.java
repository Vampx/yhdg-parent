package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeptServiceTest extends BaseJunit4Test {

    @Autowired
    DeptService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        assertNotNull(service.find(dept.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        assertTrue(1 == service.findPage(dept).getTotalItems());
        assertTrue(1 == service.findPage(dept).getResult().size());
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());

        assertEquals(1, service.insert(dept));
    }


    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        Dept dept1 = new Dept();
        dept1.setId(dept.getId());
        dept1.setMemo("memo");
        dept1.setDeptName("jio");

        assertEquals(1, service.update(dept1));
    }

    @Test
    public void hasRef1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        Role role = newRole(agent.getId());
        insertRole(role);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertEquals(String.format("部门下有用户:%s不能删除", user.getLoginName()), service.hasRef(dept.getId()).getMessage());
    }

    @Test
    public void hasRef2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        Role role = newRole(agent.getId());
        insertRole(role);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertTrue(service.hasRef(dept.getId() + 1).isSuccess());
    }

    @Test
    public void delete1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        Role role = newRole(agent.getId());
        insertRole(role);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);
        assertEquals(String.format("部门下有用户:%s不能删除", user.getLoginName()), service.delete(dept.getId()).getMessage());
    }

    @Test
    public void delete2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        Role role = newRole(agent.getId());
        insertRole(role);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        assertTrue(service.delete(dept.getId() + 1).isSuccess());
    }
}
