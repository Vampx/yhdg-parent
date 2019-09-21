package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceReportLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.DeviceReportLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceReportLogService {
    @Autowired
    private DeviceReportLogMapper deviceReportLogMapper;

    public Page findPage(DeviceReportLog deviceReportLog) {
        Page page = deviceReportLog.buildPage();
        page.setTotalItems(deviceReportLogMapper.findPageCount(deviceReportLog));
        deviceReportLog.setBeginIndex(page.getOffset());
        page.setResult(deviceReportLogMapper.findPageResult(deviceReportLog));
        return page;
    }

}
