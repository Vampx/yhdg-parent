package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalRunLog;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalRunLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalRunLogService {
    @Autowired
    TerminalRunLogMapper terminalRunLogMapper;
    public int insert(TerminalRunLog log) {
        return terminalRunLogMapper.insert(log);
    }

}
