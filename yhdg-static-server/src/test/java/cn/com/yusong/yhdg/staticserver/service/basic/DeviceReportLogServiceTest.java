package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceReportLog;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceReportLogServiceTest extends BaseJunit4Test {

    @Autowired
    DeviceReportLogService deviceReportLogService;

    @Test
    public void insert() {
        DeviceReportLog log = newDeviceReportLog(1, "001");
        deviceReportLogService.insert(log);
    }
}
