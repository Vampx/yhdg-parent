package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalCommandMapper;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCommand;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalCommandService {
    @Autowired
    TerminalCommandMapper terminalCommandMapper;

    public TerminalCommand find(Long id) {
        return terminalCommandMapper.find(id);
    }

    public Page findPage(TerminalCommand search) {
        Page page = search.buildPage();
        page.setTotalItems(terminalCommandMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<TerminalCommand> list = terminalCommandMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    public int create(TerminalCommand terminalCommand) {
        return terminalCommandMapper.insert(terminalCommand);
    }
}
