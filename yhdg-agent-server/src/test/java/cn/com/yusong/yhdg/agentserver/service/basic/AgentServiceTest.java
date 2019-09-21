package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by chen on 2017/9/4.
 */
public class AgentServiceTest extends BaseJunit4Test {
    @Autowired
    AgentService agentService;
    @Autowired
    MemCachedClient memCachedClient;
    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        assertNotNull(agentService.find(agent.getId()));
    }

    @Test
    public void findAll(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        List<Agent> agentList = agentService.findAll();
       // assertEquals(2, agentList.size());
        assertNotNull(agentList);
    }

    @Test
    public void topAgentList(){
        assertNotNull(agentService.topAgentList());
    }

//    @Test
//    public void selfPlatformAgentList() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        agent.setIsSelfPlatform(ConstEnum.Flag.TRUE.getValue());
//        insertAgent(agent);
//
//        assertTrue(1 == agentService.selfPlatformAgentList().size());
//
//    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        assertTrue(1 == agentService.findPage(agent).getTotalItems());
        assertTrue(1 == agentService.findPage(agent).getResult().size());

    }

    @Test
    public void findPageTree() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        agent.setParentId(1);
        insertAgent(agent);

        assertTrue(1 == agentService.findPageTree(agent).getTotalItems());
        assertTrue(1 == agentService.findPageTree(agent).getResult().size());

    }
    @Test
    public void childPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        agent.setParentId(1);
        insertAgent(agent);

        assertTrue(1 == agentService.childPage(agent.getParentId()).size());

    }
    @Test
    public void update(){
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Agent a = new Agent();
        a.setId(agent.getId());
        a.setOrderNum(1);
        a.setIsActive(1);
        a.setAgentName("xxxx");
        a.setPartnerId(partner.getId());
        agentService.update(a);

        Agent c =agentService.find(a.getId());
        assertEquals(a.getAgentName(), c.getAgentName());
        assertEquals(a.getOrderNum(), c.getOrderNum());
        assertEquals(a.getIsActive(), c.getIsActive());

        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, agent.getId());
        Agent result = (Agent)memCachedClient.get(key);
        assertNull(result);
    }


    @Test
    public void agentStats() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);




        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);
        Shop shop = newShop(agent.getId());
        insertShop(shop);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        FaultLog faultLog = newFaultLog(agent.getId());
        insertFaultLog(faultLog);

    }


    @Test
    public void insert(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        agentService.insert(agent);
        Agent a = agentService.find(agent.getId());
        assertNotNull(a);
    }
    @Test
    public void delete(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        agentService.delete(agent.getId());

        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, agent.getId());
        Agent result = (Agent)memCachedClient.get(key);
        assertNull(result);
    }

    @Test
    public void updateOrderNum(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        assertEquals(1, agentService.updateOrderNum(agent.getId(), 2));
    }

//    @Test
//    public void updateIsSelfBalance() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//        assertEquals(1, agentService.updateIsSelfBalance(agent.getId(),ConstEnum.Flag.FALSE.getValue()));
//    }


}
