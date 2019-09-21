package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalDownloadProgressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalDownloadProgressService {
    @Autowired
    TerminalDownloadProgressMapper terminalDownloadProgressMapper;

    public String findPlaylistProgressInfo(String id) {
        return terminalDownloadProgressMapper.findPlaylistProgressInfo(id);
    }
}
