package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class AgentBatteryTypeServiceTest extends BaseJunit4Test {

    @Autowired
    AgentBatteryTypeService agentBatteryTypeService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        assertNotNull(agentBatteryTypeService.find(battery.getType(), agent.getId()));
    }

    @Test
    public void findListByCabinetId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(), battery.getType());
        insertCabinetBatteryType(cabinetBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        agentBatteryType.setCabinetId(cabinet.getId());
        insertAgentBatteryType(agentBatteryType);

        assertTrue(1 == agentBatteryTypeService.findListByCabinetId(cabinet.getId()).size());
    }

    @Test
    public void findForName() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        assertNotNull(agentBatteryTypeService.findForName(battery.getType(), agent.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        assertTrue(1 == agentBatteryTypeService.findPage(agentBatteryType).getTotalItems());
        assertTrue(1 == agentBatteryTypeService.findPage(agentBatteryType).getResult().size());
    }

    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        assertTrue(agentBatteryTypeService.create(agentBatteryType).isSuccess());
        assertNotNull(agentBatteryTypeService.find(agentBatteryType.getBatteryType(), agentBatteryType.getAgentId()));
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        SystemBatteryType systemBatteryType2 = newSystemBatteryType();
        systemBatteryType2.setId((systemBatteryType.getId()+1)%127);
        insertSystemBatteryType(systemBatteryType2);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        agentBatteryType.setToBatteryType(systemBatteryType2.getId());
        insertAgentBatteryType(agentBatteryType);

        agentBatteryType.setTypeName("测试用的typeName");

        assertTrue(agentBatteryTypeService.update(agentBatteryType).isSuccess());
        String dbTypeName = jdbcTemplate.queryForObject("select type_name from bas_agent_battery_type where battery_type = " + agentBatteryType.getToBatteryType() + " and agent_id = " + agentBatteryType.getAgentId(), String.class);
        assertEquals(agentBatteryType.getTypeName(), dbTypeName);
    }

    @Test
    public void bindCabinet() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertEquals(1, agentBatteryTypeService.bindCabinet(battery.getType(), cabinet.getId()));

    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        assertTrue(agentBatteryTypeService.delete(battery.getType(), agent.getId()).isSuccess());
        assertNull(agentBatteryTypeService.find(agentBatteryType.getBatteryType(), agentBatteryType.getAgentId()));
    }

}
