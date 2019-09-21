package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
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
    public void findByPartner() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        List<Integer> byPartner = agentService.findByPartner(partner.getId());
        assertTrue(1 == byPartner.size());
        assertEquals(agent.getId().intValue(), byPartner.get(0).intValue());
    }

    @Test
    public void findByWeixinmp() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        List<Integer> byWeixinmp = agentService.findByWeixinmp(weixinmp.getId());
        assertTrue(1 == byWeixinmp.size());
        assertEquals(agent.getId(), byWeixinmp.get(0));
    }

    @Test
    public void findByAlipayfw() {
        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        alipayfw.setAuthType(Alipayfw.AuthType.AUTO.getValue());
        insertAlipayfw(alipayfw);

        Agent agent = newAgent(partner.getId());
        agent.setAlipayfwId(alipayfw.getId());
        insertAgent(agent);

        List<Integer> byAlipayfw = agentService.findByAlipayfw(alipayfw.getId());
        assertTrue(1 == byAlipayfw.size());
        assertEquals(agent.getId(), byAlipayfw.get(0));
    }

    @Test
    public void findByPhoneapp() {
        Partner partner = newPartner();
        insertPartner(partner);

        Phoneapp phoneapp = newPhoneapp(partner.getId());
        phoneapp.setAuthType(Phoneapp.AuthType.AUTO.getValue());
        insertPhoneapp(phoneapp);

        Agent agent = newAgent(partner.getId());
        agent.setPhoneappId(phoneapp.getId());
        insertAgent(agent);

        List<Integer> byPhoneapp = agentService.findByPhoneapp(phoneapp.getId());
        assertTrue(1 == byPhoneapp.size());
        assertEquals(agent.getId(), byPhoneapp.get(0));
    }
}
