package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceReportLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface DeviceReportLogMapper extends MasterMapper {

    public int insert(DeviceReportLog log);
}
