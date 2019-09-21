package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void findForegift() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        agent.setForegiftBalance(500);
        agent.setZdForegiftBalance(500);
        agent.setBalance(1000);
        insertAgent(agent);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue());
        agentSystemConfig.setConfigValue(String.valueOf(10));
        insertAgentSystemConfig(agentSystemConfig);

        Agent foregiftAgent = agentService.findForegift(agent.getId());
        Integer hdWithdrawMoney = foregiftAgent.getHdWithdrawMoney();
        Integer zdWithdrawMoney = foregiftAgent.getZdWithdrawMoney();

        int foregiftRemainMoney = jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId());
        int zdForegiftRemainMoney = jdbcTemplate.queryForInt("select zd_foregift_remain_money from bas_agent where id = ?", agent.getId());
        int foregiftBalance = jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId());
        int zdForegiftBalance = jdbcTemplate.queryForInt("select zd_foregift_balance from bas_agent where id = ?", agent.getId());


        int floorBalance = foregiftBalance * 10 / 100;
        int zdFloorBalance = zdForegiftBalance * 10 / 100;

        Integer testHdWithdrawMoney = foregiftRemainMoney - floorBalance;
        Integer testZdWithdrawMoney = zdForegiftRemainMoney - zdFloorBalance;

        assertEquals(testHdWithdrawMoney, hdWithdrawMoney);
        assertEquals(testZdWithdrawMoney, zdWithdrawMoney);
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
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Agent a = new Agent();
        a.setId(agent.getId());
        a.setPartnerId(partner.getId());
        a.setOrderNum(3);
        a.setIsActive(1);
        a.setAgentName("xxxx");
//        a.setIsSelfBalance(ConstEnum.Flag.TRUE.getValue());
        agentService.update(a);

        Agent c =agentService.find(a.getId());
        assertEquals(a.getAgentName(), c.getAgentName());
//        assertEquals(a.getOrderNum(), c.getOrderNum());
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
