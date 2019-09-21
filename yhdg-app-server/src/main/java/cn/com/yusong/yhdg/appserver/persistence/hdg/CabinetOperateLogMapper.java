package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CabinetOperateLogMapper extends MasterMapper {

    public void insert(CabinetOperateLog log);
}
