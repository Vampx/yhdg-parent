package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryReportLogMapper;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BatteryReportLogService {
    @Autowired
    BatteryReportLogMapper batteryReportLogMapper;

    public BatteryReportLog find(String batteryId, Date reportTime) {
        String suffix = DateFormatUtils.format(reportTime, "yyyyww");
        BatteryReportLog batteryReportLog = new BatteryReportLog();
        batteryReportLog.setBatteryId(batteryId);
        batteryReportLog.setSuffix(suffix);
        batteryReportLog.setReportTime(reportTime);
        return batteryReportLogMapper.find(batteryReportLog);
    }

    public Page findPage(BatteryReportLog search) {

        Page page = search.buildPage();
        if (search.getReportTime() == null) {
            search.setReportTime(new Date());
        }
        String suffix = DateFormatUtils.format(search.getReportTime(), "yyyyww");
        search.setSuffix(suffix);
        page.setTotalItems(batteryReportLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryReportLog> batteryReportLogs = batteryReportLogMapper.findPageResult(search);
        page.setResult(batteryReportLogs);

        return page;
    }

    public List<BatteryReportLog> findForExcel(BatteryReportLog search) {
        if (search.getReportTime() == null) {
            search.setReportTime(new Date());
        }
        String suffix = DateFormatUtils.format(search.getReportTime(), "yyyyww");
        search.setSuffix(suffix);
        List<BatteryReportLog> pageResult = batteryReportLogMapper.findPageResult(search);
        return pageResult;
    }

    public List<BatteryReportLog> findList(String batteryId, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, "yyyyww");
        return batteryReportLogMapper.findList(batteryId, createTime, suffix);
    }
}
