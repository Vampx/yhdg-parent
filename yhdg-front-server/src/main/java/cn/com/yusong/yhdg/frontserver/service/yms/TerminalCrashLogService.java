package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalCrashLog;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalCrashLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalCrashLogService {
    @Autowired
    TerminalCrashLogMapper terminalCrashLogMapper;

    public int insert(TerminalCrashLog crashLog) {
        return terminalCrashLogMapper.insert(crashLog);
    }
}
