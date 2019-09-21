package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalCommand;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface TerminalCommandMapper extends MasterMapper {
    TerminalCommand find(long id);

    int findPageCount(TerminalCommand search);

    List<TerminalCommand> findPageResult(TerminalCommand search);

    int insert(TerminalCommand terminalCommand);

}
