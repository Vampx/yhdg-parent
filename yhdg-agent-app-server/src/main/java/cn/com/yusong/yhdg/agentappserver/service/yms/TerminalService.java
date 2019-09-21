package cn.com.yusong.yhdg.agentappserver.service.yms;

import cn.com.yusong.yhdg.agentappserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService extends AbstractService {
    @Autowired
    TerminalMapper terminalMapper;

    public Terminal find(String id) {
        return terminalMapper.find(id);
    }

    public List<Terminal> findList(Integer isOnline, int offset, int limit) {
        return terminalMapper.findList(isOnline, offset, limit);
    }

    public int findTerminalCount(Integer isOnline){
        return terminalMapper.findTerminalCount(isOnline);
    }
}
