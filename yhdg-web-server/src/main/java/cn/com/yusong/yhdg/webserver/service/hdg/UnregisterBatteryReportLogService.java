package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.UnregisterBatteryReportLogMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UnregisterBatteryReportLogService {
    @Autowired
    UnregisterBatteryReportLogMapper unregisterBatteryReportLogMapper;

    public Page findPage(UnregisterBatteryReportLog search) {
        Page page = search.buildPage();
        String suffix = DateFormatUtils.format(search.getQueryLogTime(), "yyyyww");
        if(StringUtils.isNotEmpty(unregisterBatteryReportLogMapper.findTableExist("hdg_unregister_battery_report_log_"+suffix))) {
            search.setSuffix(suffix);
            page.setTotalItems(unregisterBatteryReportLogMapper.findPageCount(search));
            search.setBeginIndex(page.getOffset());
            page.setResult(unregisterBatteryReportLogMapper.findPageResult(search));
        }
        return page;
    }

    public UnregisterBatteryReportLog find(String code, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, "yyyyww");
        UnregisterBatteryReportLog unregisterBatteryReportLog = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(unregisterBatteryReportLogMapper.findTableExist("hdg_unregister_battery_report_log_"+suffix))) {
            unregisterBatteryReportLog = new UnregisterBatteryReportLog();
            unregisterBatteryReportLog.setCode(code);
            unregisterBatteryReportLog.setSuffix(suffix);
            unregisterBatteryReportLog.setCreateTime(createTime);
        }

        return unregisterBatteryReportLogMapper.find(unregisterBatteryReportLog);
    }
}
