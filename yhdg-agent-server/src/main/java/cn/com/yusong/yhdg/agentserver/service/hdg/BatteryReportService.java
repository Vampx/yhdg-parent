package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryReportDateMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReport;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportDate;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.GPSUtil;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryReportMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class BatteryReportService {
    @Autowired
    BatteryReportMapper batteryReportMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryReportDateMapper batteryReportDateMapper;

    public BatteryReport find(String batteryId, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, "yyyyww");
        BatteryReport batteryReport = new BatteryReport();
        batteryReport.setBatteryId(batteryId);
        batteryReport.setSuffix(suffix);
        batteryReport.setCreateTime(createTime);
        return batteryReportMapper.find(batteryReport);
    }

    private void makeTableData(BatteryReport search, String suffix, Page page) {
        search.setSuffix(suffix);
        page.setTotalItems(batteryReportMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryReport> batteryReportList = batteryReportMapper.findPageResult(search);
        for (BatteryReport batteryReport : batteryReportList) {
            if (StringUtils.isNotEmpty(batteryReport.getCabinetId())) {
                Cabinet cabinet = cabinetMapper.find(batteryReport.getCabinetId());
                if (cabinet != null) {
                    batteryReport.setCabinetName(cabinet.getCabinetName());
                }
            }
            if (batteryReport.getLat() != null && batteryReport.getLng() != null && batteryReport.getLat() != 0.0 && batteryReport.getLng() != 0.0) {
                double[] d = GPSUtil.gps84_To_bd09( batteryReport.getLat(),batteryReport.getLng());
                batteryReport.setBdLng(d[1]);
                batteryReport.setBdLat(d[0]);
            }
        }
        page.setResult(batteryReportList);
    }

    public Page findPage(BatteryReport search) throws ParseException {
        Page page = search.buildPage();
        //一开始的请求没有带上时间
        if (search.getCreateTime() == null) {
            search.setCreateTime(new Date());

            String suffix = DateFormatUtils.format(search.getCreateTime(), "yyyyww");
            //查询表是否存在
            if (StringUtils.isNotEmpty(batteryReportMapper.findTableExist(BatteryReport.BATTERY_REPORT_TABLE_NAME + suffix))) {
                makeTableData(search, suffix, page);
            } else {
                //表不存在，查询最近的上报日期
                BatteryReportDate batteryReportDate = batteryReportDateMapper.findLast(search.getBatteryId());
                if (batteryReportDate != null) {
                    if (StringUtils.isNotEmpty(batteryReportDate.getReportDate())) {
                        Date date = DateUtils.parseDate(batteryReportDate.getReportDate(), new String[]{Constant.DATE_FORMAT});
                        search.setCreateTime(date);
                        String dataSuffix = DateFormatUtils.format(date, "yyyyww");
                        if (StringUtils.isNotEmpty(batteryReportMapper.findTableExist(BatteryReport.BATTERY_REPORT_TABLE_NAME + dataSuffix))) {
                            makeTableData(search, dataSuffix, page);
                        }
                    }
                }
            }
        } else {
            //带上时间的查询
            String suffix = DateFormatUtils.format(search.getCreateTime(), "yyyyww");
            //查询表是否存在
            if (StringUtils.isNotEmpty(batteryReportMapper.findTableExist(BatteryReport.BATTERY_REPORT_TABLE_NAME + suffix))) {
                makeTableData(search, suffix, page);
            }
        }
        return page;
    }

    public List<BatteryReport> findForExcel(BatteryReport search) {
        if (search.getCreateTime() == null) {
            search.setCreateTime(new Date());
        }
        String suffix = DateFormatUtils.format(search.getCreateTime(), "yyyyww");
        search.setSuffix(suffix);
        List<BatteryReport> pageResult = batteryReportMapper.findPageResult(search);
        return pageResult;
    }

    public List<BatteryReport> findList(String batteryId, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, "yyyyww");
        return batteryReportMapper.findList(batteryId, createTime, suffix);
    }

    public BatteryReport findRecentInfo(String batteryId) {
        Date date = new Date();
        BatteryReport dbBatteryReport = null;
        for (int i = 0; i < 30; i++) {
            String suffix = DateFormatUtils.format(date, "yyyyww");
            String tableName = batteryReportMapper.findTableExist("hdg_battery_report_" + suffix);
            if (StringUtils.isNotEmpty(tableName)) {
                BatteryReport batteryReport = batteryReportMapper.findLast(batteryId, suffix);
                if (batteryReport != null && batteryReport.getHeartType() == BatteryReport.HeartType.NORMAL_HEART.getValue()) {
                    dbBatteryReport = batteryReport;
                    break;
                }
            }
            date = DateUtils.addDays(date, -1);
        }
        return dbBatteryReport;
    }
}
