package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RolePermServiceTest extends BaseJunit4Test {

    @Autowired
    RolePermService service;

    @Test
    public void findAll() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Menu menu = newMenu(null);
        insertMenu(menu);

        Perm perm = newPerm(menu.getId());
        insertPerm(perm);

        RolePerm rolePerm = newRolePerm(role.getId(), perm.getId());
        insertRolePerm(rolePerm);

        assertEquals("not equals", 1, service.findAll(role.getId()).size());
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Menu menu = newMenu(null);
        insertMenu(menu);

        Perm perm = newPerm(menu.getId());
        insertPerm(perm);

        RolePerm rolePerm = newRolePerm(role.getId(), perm.getId());
        insertRolePerm(rolePerm);

        assertTrue("delete fail", 1 == service.delete(role.getId()));
    }



}
