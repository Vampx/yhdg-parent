package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalOnlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TerminalOnlineService {

    @Autowired
    TerminalOnlineMapper terminalOnlineMapper;
    @Autowired
    TerminalMapper terminalMapper;

    public int updateHeartInfo(TerminalOnline online) {
        return terminalOnlineMapper.updateHeartInfo(online);
    }

    public int updateLoginInfo(String id, Integer isOnline, Date heartTime) {
        return terminalOnlineMapper.updateLoginInfo(id, isOnline, heartTime);
    }

    public int updatePlayFile(String id, String playFile) {
        return terminalOnlineMapper.updatePlayFile(id, playFile);
    }

    public int updateDownloadPlaylistProgress(String id, float speed, float downloadPlaylistProgress) {
        return terminalOnlineMapper.updateDownloadPlaylistProgress(id, speed, downloadPlaylistProgress);
    }

    public int insert(TerminalOnline online) {
        online.setIsNormal(ConstEnum.Flag.TRUE.getValue());
        return terminalOnlineMapper.insert(online);
    }
}
