package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDynamicCodeCustomer;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetDynamicCodeCustomerServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetDynamicCodeCustomerService cabinetDynamicCodeCustomerService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), null, playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CabinetDynamicCodeCustomer cabinetDynamicCodeCustomer = newCabinetDynamicCodeCustomer(cabinet.getId(), customer.getId());
        insertCabinetDynamicCodeCustomer(cabinetDynamicCodeCustomer);

        assertNotNull(cabinetDynamicCodeCustomerService.find(cabinetDynamicCodeCustomer.getCabinetId(), cabinetDynamicCodeCustomer.getCustomerId()));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), null, playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CabinetDynamicCodeCustomer cabinetDynamicCodeCustomer = newCabinetDynamicCodeCustomer(cabinet.getId(), customer.getId());

        assertEquals(1, cabinetDynamicCodeCustomerService.insert(cabinetDynamicCodeCustomer));
    }
}
