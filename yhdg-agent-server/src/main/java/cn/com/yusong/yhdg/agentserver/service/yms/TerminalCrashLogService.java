package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalCrashLogMapper;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCrashLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalCrashLogService {
    @Autowired
    TerminalCrashLogMapper terminalCrashLogMapper;

    public TerminalCrashLog find(long id) {
        return terminalCrashLogMapper.find(id);
    }

    public Page findPage(TerminalCrashLog search) {
        Page page = search.buildPage();
        page.setTotalItems(terminalCrashLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(terminalCrashLogMapper.findPageResult(search));
        return page;
    }

    public int insert(TerminalCrashLog log) {
        return terminalCrashLogMapper.insert(log);
    }
}
