package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalDownloadProgress;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalDownloadProgressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalDownloadProgressService {

    @Autowired
    TerminalDownloadProgressMapper terminalDownloadProgressMapper;

    public int insert(TerminalDownloadProgress progress) {
        return terminalDownloadProgressMapper.insert(progress);
    }

    public int update(TerminalDownloadProgress progress) {
        return terminalDownloadProgressMapper.update(progress);
    }

    public TerminalDownloadProgress find(String id) {
        return terminalDownloadProgressMapper.find(id);
    }
}
