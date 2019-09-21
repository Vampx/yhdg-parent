package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalOnlineMapper;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalOnlineService extends AbstractService {

    @Autowired
    TerminalOnlineMapper terminalOnlineMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    TerminalService terminalService;

    public TerminalOnline find(String id) {
        TerminalOnline terminalOnline = terminalOnlineMapper.find(id);
        return terminalOnline;
    }

    public Page findPage(TerminalOnline terminalOnline) {
        Page<TerminalOnline> page = terminalOnline.buildPage();
        page.setTotalItems(terminalOnlineMapper.findPageCount(terminalOnline));
        terminalOnline.setBeginIndex(page.getOffset());

        List<TerminalOnline> onlineList = terminalOnlineMapper.findPageResult(terminalOnline);
        for (TerminalOnline terminalOnline1: onlineList) {
            if (terminalOnline1.getAgentId() != null) {
                terminalOnline1.setAgentName(findAgentInfo(terminalOnline1.getAgentId()).getAgentName());
            }
            Terminal terminal = terminalService.find(terminalOnline1.getId());
            if (terminal != null) {
                terminalOnline1.setVersion(terminal.getVersion());
            }
        }
        page.setResult(onlineList);
        return page;
    }

    public int offline(String terminalId) {
        return terminalOnlineMapper.offline(terminalId);
    }
}
