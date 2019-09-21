package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class CabinetInstallRecordServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetInstallRecordService service;

    CabinetInstallRecord cabinetInstallRecord;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner(); insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        cabinetInstallRecord = newCabinetInstallRecord(agent.getId(), cabinet.getId());

    }

    @Test
    public void find() {
        insertCabinetInstallRecord(cabinetInstallRecord);

        assertNotNull(service.find(cabinetInstallRecord.getId()));
    }

    @Test
    public void findPage() {
        insertCabinetInstallRecord(cabinetInstallRecord);

        assertTrue(1 == service.findPage(cabinetInstallRecord).getTotalItems());
        assertTrue(1 == service.findPage(cabinetInstallRecord).getResult().size());
    }

    @Test
    public void updateUpLine() {
        cabinetInstallRecord.setStatus(CabinetInstallRecord.Status.APPROVE.getValue());
        insertCabinetInstallRecord(cabinetInstallRecord);

        cabinetInstallRecord.setRentMoney(4890203);
        assertTrue(service.updateUpLine(cabinetInstallRecord).isSuccess());
        assertEquals(cabinetInstallRecord.getRentMoney(), service.find(cabinetInstallRecord.getId()).getRentMoney());

        int s = jdbcTemplate.queryForInt("select up_line_status from hdg_cabinet where id = ?", cabinetInstallRecord.getCabinetId());
        System.out.println(s);
    }
}