package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AgentBatteryTypeServiceTest extends BaseJunit4Test {
    @Autowired
    private AgentBatteryTypeService service;

    private AgentBatteryType agentBatteryType;
    private Agent agent;

    @Before
    public void setUp() {
        Partner partner = newPartner();
        insertPartner(partner);

        agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
    }

    @Test
    public void find() {
        insertAgentBatteryType(agentBatteryType);

        assertNotNull(service.find(agentBatteryType.getBatteryType(), agentBatteryType.getAgentId()));
    }

    @Test
    public void findListByCabinetId() {
        Customer customer = newCustomer(agent.getPartnerId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(), agentBatteryType.getBatteryType());
        insertCabinetBatteryType(cabinetBatteryType);

        insertAgentBatteryType(agentBatteryType);

        assertTrue(1 == service.findListByCabinetId(cabinet.getId()).size());
    }

    @Test
    public void findForName() {
        insertAgentBatteryType(agentBatteryType);

        assertNotNull(service.findForName(agentBatteryType.getBatteryType(), agent.getId()));
    }

    @Test
    public void findPage() {
        insertAgentBatteryType(agentBatteryType);

        assertTrue(1 == service.findPage(agentBatteryType).getTotalItems());
        assertTrue(1 == service.findPage(agentBatteryType).getResult().size());
    }

    @Test
    public void create() {
        assertTrue(service.create(agentBatteryType).isSuccess());
        assertNotNull(service.find(agentBatteryType.getBatteryType(), agentBatteryType.getAgentId()));
    }

    @Test
    public void update() {
        SystemBatteryType systemBatteryType2 = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType2);

        agentBatteryType.setToBatteryType(systemBatteryType2.getId());
        insertAgentBatteryType(agentBatteryType);

        agentBatteryType.setTypeName("测试用的typeName");

        assertTrue(service.update(agentBatteryType).isSuccess());
        String dbTypeName = jdbcTemplate.queryForObject("select type_name from bas_agent_battery_type where battery_type = " + agentBatteryType.getToBatteryType() + " and agent_id = " + agentBatteryType.getAgentId(), String.class);
        assertEquals(agentBatteryType.getTypeName(), dbTypeName);
    }

    @Test
    public void bindCabinet() {
        Customer customer = newCustomer(agent.getPartnerId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertEquals(1, service.bindCabinet(agentBatteryType.getBatteryType(), cabinet.getId()));
    }

    @Test
    public void delete() {
        insertAgentBatteryType(agentBatteryType);

        assertTrue(service.delete(agentBatteryType.getBatteryType(), agent.getId()).isSuccess());
        assertNull(service.find(agentBatteryType.getBatteryType(), agentBatteryType.getAgentId()));
    }
}