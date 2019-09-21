package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalRunLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.springframework.stereotype.Component;

@Component("ymsTerminalRunLogMapper")
public interface TerminalRunLogMapper extends MasterMapper {
    int insert(TerminalRunLog log);
}
