package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CabinetOperateLogMapper extends MasterMapper {
    void insert(CabinetOperateLog log);
}
