package cn.com.yusong.yhdg.frontserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalProperty;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("TerminalPropertyMapper")
public interface TerminalPropertyMapper extends MasterMapper {

    List<TerminalProperty> findByTerminal(String terminalId);
}
