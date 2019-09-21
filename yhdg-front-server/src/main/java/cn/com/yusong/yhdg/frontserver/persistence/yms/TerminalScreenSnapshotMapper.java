package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalScreenSnapshot;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface TerminalScreenSnapshotMapper extends MasterMapper {
    int insert(TerminalScreenSnapshot snapshot);
}
