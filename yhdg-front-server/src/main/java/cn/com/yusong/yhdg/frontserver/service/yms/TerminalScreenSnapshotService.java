package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalScreenSnapshot;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalScreenSnapshotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalScreenSnapshotService {
    @Autowired
    TerminalScreenSnapshotMapper terminalScreenSnapshotMapper;

    public int insert(TerminalScreenSnapshot snapshot) {
        return terminalScreenSnapshotMapper.insert(snapshot);
    }
}
