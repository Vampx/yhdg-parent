package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.AgentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
public class AgentServiceTest extends BaseJunit4Test {

    @Autowired
    AgentService agentService;

    @Test
    public void find(){
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Agent agent1 = agentService.find(agent.getId());
        assertNotNull(agent1);
    }


}
