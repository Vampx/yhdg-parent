package cn.com.yusong.yhdg.appserver.service.yms;

import cn.com.yusong.yhdg.appserver.persistence.yms.TerminalOnlineMapper;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.domain.yms.TerminalSequence;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalOnlineService {

    @Autowired
    TerminalOnlineMapper terminalOnlineMapper;

    public int insert(TerminalOnline terminalOnline) {
        return terminalOnlineMapper.insert(terminalOnline);
    }
}
