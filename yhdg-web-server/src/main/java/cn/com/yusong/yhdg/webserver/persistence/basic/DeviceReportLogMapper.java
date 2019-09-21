package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceReportLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface DeviceReportLogMapper extends MasterMapper {

    int findPageCount(DeviceReportLog deviceReportLog);

    List<DeviceReportLog> findPageResult(DeviceReportLog deviceReportLog);

}
