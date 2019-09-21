package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReport;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.UnregisterBatteryReportMapper;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UnregisterBatteryReportService {
    @Autowired
    UnregisterBatteryReportMapper unregisterBatteryReportMapper;

    public Page findPage(UnregisterBatteryReport search) {
        Page page = search.buildPage();
        String suffix = DateFormatUtils.format(search.getQueryLogTime(), "yyyyww");
        search.setCreateTime(search.getQueryLogTime());
        if(StringUtils.isNotEmpty(unregisterBatteryReportMapper.findTableExist("hdg_unregister_battery_report_"+suffix))) {
            search.setSuffix(suffix);
            page.setTotalItems(unregisterBatteryReportMapper.findPageCount(search));
            search.setBeginIndex(page.getOffset());
            page.setResult(unregisterBatteryReportMapper.findPageResult(search));
        }
        return page;
    }

    public UnregisterBatteryReport find(String code, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, "yyyyww");
        UnregisterBatteryReport unregisterBatteryReport = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(unregisterBatteryReportMapper.findTableExist("hdg_unregister_battery_report_"+suffix))) {
            unregisterBatteryReport = new UnregisterBatteryReport();
            unregisterBatteryReport.setCode(code);
            unregisterBatteryReport.setSuffix(suffix);
            unregisterBatteryReport.setCreateTime(createTime);
        }

        return unregisterBatteryReportMapper.find(unregisterBatteryReport);
    }
}
