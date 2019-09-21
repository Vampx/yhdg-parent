package cn.com.yusong.yhdg.appserver.service.yms;

import cn.com.yusong.yhdg.appserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService extends AbstractService {
    @Autowired
    TerminalMapper terminalMapper;

    public Terminal find(String id) {
        return terminalMapper.find(id);
    }

    public int insert(Terminal entity) {
        return terminalMapper.insert(entity);
    }

    public int updateOnline(Terminal entity) {
        return terminalMapper.updateOnline(entity);
    }

    public int updateHeartPlaylistId(Terminal entity) {
        return terminalMapper.updateHeartPlaylistId(entity);
    }

    public int updateHeartTime(Terminal entity) {
        return terminalMapper.updateHeartTime(entity);
    }
}
