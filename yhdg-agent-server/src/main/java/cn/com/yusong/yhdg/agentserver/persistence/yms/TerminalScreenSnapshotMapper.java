package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalScreenSnapshot;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface TerminalScreenSnapshotMapper extends MasterMapper {
    int findPageCount(TerminalScreenSnapshot search);

    List<TerminalScreenSnapshot> findPageResult(TerminalScreenSnapshot search);
}
