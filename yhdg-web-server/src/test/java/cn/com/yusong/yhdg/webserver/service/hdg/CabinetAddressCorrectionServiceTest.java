package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CabinetAddressCorrectionServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetAddressCorrectionService service;

    @Test
    public void find() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setAddress("xxxxx");

        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);

        insertCabinet(cabinet);

        CabinetAddressCorrection correction = newCabinetAddressCorrection(cabinet, customer);
        insertCabinetAddressCorrection(correction);

        assertNotNull(service.find(correction.getId()));
    }

    @Test
    public void findPage() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setAddress("xxxxx");

        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);

        insertCabinet(cabinet);

        CabinetAddressCorrection correction = newCabinetAddressCorrection(cabinet, customer);
        insertCabinetAddressCorrection(correction);

        assertTrue(1 == service.findPage(correction).getTotalItems());
        assertTrue(1 == service.findPage(correction).getResult().size());
    }

    @Test
    public void updateStatus() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setAddress("xxxxx");

        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);

        insertCabinet(cabinet);

        CabinetAddressCorrection correction = newCabinetAddressCorrection(cabinet, customer);
        correction.setStatus(CabinetAddressCorrection.Status.AUDIT_PASS.getValue());
        insertCabinetAddressCorrection(correction);

        assertTrue(service.updateStatus(correction).isSuccess());
    }

    @Test
    public void delete() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setAddress("xxxxx");

        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);

        insertCabinet(cabinet);

        CabinetAddressCorrection correction = newCabinetAddressCorrection(cabinet, customer);
        insertCabinetAddressCorrection(correction);

        assertTrue(service.delete(correction.getId()).isSuccess());
    }

}

