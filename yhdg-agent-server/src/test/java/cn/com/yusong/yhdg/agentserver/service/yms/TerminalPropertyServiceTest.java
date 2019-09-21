package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TerminalPropertyServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalPropertyService terminalPropertyService;

    @Test
    public void findByTerminal() throws Exception {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalProperty terminalProperty = newTerminalProperty(terminal.getId(), 1);
        insertTerminalProperty(terminalProperty);

        List<TerminalProperty> list = terminalPropertyService.findByTerminal(terminal.getId());
        assertFalse(list.isEmpty());

    }

    @Test
    public void insert() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalProperty property = new TerminalProperty();
        property.setTerminalId(terminal.getId());
        property.setOrderNum(9);
        property.setPropertyName("sdfsdf");
        property.setPropertyValue("xxxxx");
        property.setIsActive(7);
        int[] a = {1,2,3};
        String[] p = {"001","002","003"};
        String[] v = {"asdas","asdsadaeredas","asdasassda"};
        assertTrue(terminalPropertyService.insert(terminal.getId(),a,p,v) > 0);
    }

}