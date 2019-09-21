package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderBatteryReportLog;
import cn.com.yusong.yhdg.common.entity.LocationInfo;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryOrderBatteryReportLogMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 换电电池上报记录
 */
@Service
public class BatteryOrderBatteryReportLogService {
    @Autowired
    BatteryOrderBatteryReportLogMapper batteryOrderBatteryReportLogMapper;

    public Page findselectPage(BatteryOrderBatteryReportLog search) {
        Page page = search.buildPage();
        if (StringUtils.isNotEmpty(batteryOrderBatteryReportLogMapper.findTableExist("hdg_battery_order_battery_report_log_" + search.getSuffix()))) {
            if (search.getQueryBeginTime() == null && search.getQueryEndTime() == null) {
                Date queryBeginTime = batteryOrderBatteryReportLogMapper.findLastReportTime(search.getOrderId(), search.getSuffix());
                queryBeginTime.setHours(0);
                queryBeginTime.setMinutes(0);
                queryBeginTime.setSeconds(0);
                search.setQueryBeginTime(queryBeginTime);
            }
            page.setTotalItems(batteryOrderBatteryReportLogMapper.findAllMapCount(search.getOrderId(), search.getSuffix(), search.getQueryBeginTime(), search.getQueryEndTime()));
            search.setBeginIndex(page.getOffset());
            page.setResult(batteryOrderBatteryReportLogMapper.findSelectPageResult(search));
        }
        return page;
    }

    public List<LocationInfo> findAllMap(String orderId, Date createTime, Date startDate, Date endDate) {
        String suffix = DateFormatUtils.format(createTime, "yyyyww");
        List<LocationInfo> result = new ArrayList<LocationInfo>();
        if (startDate == null && endDate == null) {
            startDate = batteryOrderBatteryReportLogMapper.findLastReportTime(orderId, suffix);
            startDate.setHours(0);
            startDate.setMinutes(0);
            startDate.setSeconds(0);
        }
        if (StringUtils.isNotEmpty(batteryOrderBatteryReportLogMapper.findTableExist("hdg_battery_order_battery_report_log_" + suffix))) {
            result = batteryOrderBatteryReportLogMapper.findAllMap(orderId, suffix, startDate, endDate);
        }
        return result;
    }

    public ExtResult findAllMapCount(String orderId, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, "yyyyww");
        if (StringUtils.isNotEmpty(batteryOrderBatteryReportLogMapper.findTableExist("hdg_battery_order_battery_report_log_" + suffix))) {
            int total = batteryOrderBatteryReportLogMapper.findAllMapCount(orderId, suffix, null, null);
            if (total > 0) {
                return ExtResult.successResult();
            }
        }
        return ExtResult.failResult("");
    }


    public Page findPage(BatteryOrderBatteryReportLog search) {
        Page page = search.buildPage();
        if (StringUtils.isNotEmpty(batteryOrderBatteryReportLogMapper.findTableExist("hdg_battery_order_battery_report_log_" + search.getSuffix()))) {
            page.setTotalItems(batteryOrderBatteryReportLogMapper.findPageCount(search));
            search.setBeginIndex(page.getOffset());
            List<BatteryOrderBatteryReportLog> list = batteryOrderBatteryReportLogMapper.findPageResult(search);
            page.setResult(list);
        }
        return page;
    }

    public ExtResult updateAddress(BatteryOrderBatteryReportLog batteryOrderBatteryReportLog) {
        int total = batteryOrderBatteryReportLogMapper.updateAddress(batteryOrderBatteryReportLog);
        if (total > 0) {
            return ExtResult.successResult();
        }
        return ExtResult.failResult("修改位置失败");
    }
}
