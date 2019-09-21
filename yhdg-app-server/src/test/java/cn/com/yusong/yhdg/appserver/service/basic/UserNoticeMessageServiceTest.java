package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNoticeMessageServiceTest extends BaseJunit4Test {
    @Autowired
    UserNoticeMessageService userNoticeMessageService;

    @Test
    public void findListByUserId() {
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
        UserNoticeMessage userNoticeMessage = newUserNoticeMessage(user.getId());
        insertUserNoticeMessage(userNoticeMessage);
        assertEquals(1, userNoticeMessageService.findListByUserId(user.getId(), UserNoticeMessage.Type.NOTICE.getValue(), 0, 20).size());
    }
}
