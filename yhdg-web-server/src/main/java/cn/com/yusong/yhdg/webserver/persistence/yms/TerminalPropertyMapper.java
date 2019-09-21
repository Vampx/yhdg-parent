package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalProperty;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface TerminalPropertyMapper extends MasterMapper {
    List<TerminalProperty> findByTerminal(String terminalId);

    int deleteByTerminal(String terminalId);

    int insert(TerminalProperty terminalProperty);
}
