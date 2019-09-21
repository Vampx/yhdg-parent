package cn.com.yusong.yhdg.frontserver.service.yms;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalDownloadProgress;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.frontserver.persistence.yms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TerminalService extends AbstractService{

    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    TerminalOnlineMapper terminalOnlineMapper;
    @Autowired
    TerminalStrategyMapper terminalStrategyMapper;
    @Autowired
    TerminalDownloadProgressMapper terminalDownloadProgressMapper;
    @Autowired
    PlaylistMapper playlistMapper;

    public Terminal find(String id) {
        Terminal terminal = terminalMapper.find(id);
        return terminal;
    }

    @Transactional(rollbackFor = Throwable.class)
    public int create(Terminal terminal, TerminalOnline online, TerminalDownloadProgress progress) {
        terminalMapper.insert(terminal);
        terminalOnlineMapper.insert(online);
        return terminalDownloadProgressMapper.insert(progress);
    }

    public int updateLoginInfo(String id, String version) {
        return terminalMapper.updateLoginInfo(id, version);
    }

    public List<Terminal> findByPlaylist(int playlistId) {
        return terminalMapper.findByPlaylist(playlistId);
    }

    public int offline(String id) {
        terminalOnlineMapper.offline(id, ConstEnum.Flag.FALSE.getValue());
        return terminalMapper.offline(id, ConstEnum.Flag.FALSE.getValue());
    }
}
