package cn.com.yusong.yhdg.agentserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.CabinetSimReplaceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface CabinetSimReplaceRecordMapper extends MasterMapper {
    int findPageCount(CabinetSimReplaceRecord search);

    List findPageResult(CabinetSimReplaceRecord search);
}
