package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface TerminalOnlineMapper extends MasterMapper {
    TerminalOnline find(String id);

    int findPageCount(TerminalOnline terminalOnline);

    List<TerminalOnline> findPageResult(TerminalOnline terminalOnline);

    int delete(String id);

    int offline(String id);
}
