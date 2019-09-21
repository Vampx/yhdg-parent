package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNoticeMessageServiceTest extends BaseJunit4Test {

    @Autowired
    UserNoticeMessageService service;

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

        UserNoticeMessage userNoticeMessage = newUserNoticeMessage(user.getId());
        insertUserNoticeMessage(userNoticeMessage);

        assertNotNull(service.find(userNoticeMessage.getId()));
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

        UserNoticeMessage userNoticeMessage = newUserNoticeMessage(user.getId());
        insertUserNoticeMessage(userNoticeMessage);

        assertTrue(1 == service.findPage(userNoticeMessage).getTotalItems());
        assertTrue(1 == service.findPage(userNoticeMessage).getResult().size());
    }


}
