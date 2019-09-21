package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.domain.basic.User;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AgentServiceTest extends BaseJunit4Test {

    @Autowired
    AgentService agentService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Agent agent1 = agentService.find(agent.getId());
        assertNotNull(agent1);
    }

    @Test
    public void findAllId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        assertTrue(agentService.findAllId().size() > 0);
    }

    @Test
    public void findByParent() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Agent agent1 = newAgent(partner.getId());
        agent1.setParentId(agent.getId());
        insertAgent(agent1);
        assertEquals(1, agentService.findByParent(agent1.getParentId()).size());
    }


    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Role role = newRole(agent.getId());
        insertRole(role);
        Dept dept = newDept(agent.getId());
        insertDept(dept);
        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setLoginName("aaaxxxxx");
        User[] userList = new User[]{user};
        assertEquals(0, agentService.create(agent, userList).getCode());
    }

    @Test
    public void update1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Role role = newRole(agent.getId());
        insertRole(role);
        Dept dept = newDept(agent.getId());
        insertDept(dept);
        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setLoginName("aaaxxxxx");
        User[] addUserList = new User[]{user};

        assertEquals(0, agentService.update(agent, addUserList, null, null).getCode());
    }

    @Test
    public void update2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Role role = newRole(agent.getId());
        insertRole(role);
        Dept dept = newDept(agent.getId());
        insertDept(dept);
        User user1 = newUser(agent.getId(), role.getId(), dept.getId());
        user1.setLoginName("xxxxx");
        User[] updateUserList = new User[]{user1};
        assertEquals(0, agentService.update(agent, null, updateUserList, null).getCode());
    }

    @Test
    public void update3() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Role role = newRole(agent.getId());
        insertRole(role);
        Dept dept = newDept(agent.getId());
        insertDept(dept);
        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setLoginName("aaaxxxxx");
        User user1 = newUser(agent.getId(), role.getId(), dept.getId());
        user1.setLoginName("xxxxx");
        insertUser(user);
        insertUser(user1);
        Long[] deleteUserList = new Long[]{user.getId(), user1.getId()};

        assertEquals(0, agentService.update(agent, null, null, deleteUserList).getCode());
    }

    @Test
    public void buildLevelList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        assertNotNull(agentService.buildLevelList(agent.getId()));
    }
}
