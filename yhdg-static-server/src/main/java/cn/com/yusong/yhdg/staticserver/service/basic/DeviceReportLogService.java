package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceReportLog;
import cn.com.yusong.yhdg.staticserver.persistence.basic.DeviceReportLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceReportLogService {
    @Autowired
    DeviceReportLogMapper deviceReportLogMapper;

    public int insert(DeviceReportLog log) {
        return deviceReportLogMapper.insert(log);
    }
}
