package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.GPSUtil;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryReportDateMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryReportMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
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

    public BatteryReport find(Integer id, String batteryId, Date createTime) {
        String suffix = dealSuffix(createTime);
        if (id != null) {
            return batteryReportMapper.findById(id, suffix);
        } else {
            return batteryReportMapper.findByTime(batteryId, createTime, suffix);
        }
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
            String suffix =dealSuffix(search.getCreateTime());
            if (StringUtils.isNotEmpty(batteryReportMapper.findTableExist(BatteryReport.BATTERY_REPORT_TABLE_NAME + suffix))) {
                makeTableData(search, suffix, page);
            } else {
                //表不存在，查询最近的上报日期
                BatteryReportDate batteryReportDate = batteryReportDateMapper.findLast(search.getBatteryId());
                if (batteryReportDate != null) {
                    if (StringUtils.isNotEmpty(batteryReportDate.getReportDate())) {
                        Date date = DateUtils.parseDate(batteryReportDate.getReportDate(), new String[]{Constant.DATE_FORMAT});
                        search.setCreateTime(date);
                        String dataSuffix =dealSuffix(date);
                        if (StringUtils.isNotEmpty(batteryReportMapper.findTableExist(BatteryReport.BATTERY_REPORT_TABLE_NAME + dataSuffix))) {
                            makeTableData(search, dataSuffix, page);
                        }
                    }
                }
            }
        } else {
            String suffix =dealSuffix(search.getCreateTime());
            if (StringUtils.isNotEmpty(batteryReportMapper.findTableExist(BatteryReport.BATTERY_REPORT_TABLE_NAME + suffix))) {
                makeTableData(search, suffix, page);
            }
        }
        return page;
    }

    public Page findNormalHeartReportPage(BatteryReport search) throws ParseException {
        Page page = search.buildPage();
        String suffix =dealSuffix(search.getCreateTime());
        search.setSuffix(suffix);

        if (StringUtils.isEmpty(batteryReportMapper.findTableExist(BatteryReport.BATTERY_REPORT_TABLE_NAME + suffix))) {
            List<BatteryReport> emptyList = new ArrayList<BatteryReport>();
            page.setTotalItems(0);
            page.setResult(emptyList);
            return page;
        }
        int beforePageCount = batteryReportMapper.findBeforePageCount(search);
		if (beforePageCount > 10) {
			beforePageCount = 10;
		}
		int afterPageCount = batteryReportMapper.findAfterPageCount(search);
		if (afterPageCount > 10) {
			afterPageCount = 10;
		}
		search.setBeginIndex(0);
		search.setRows(10);
		List<BatteryReport> batteryBeforeReportList = batteryReportMapper.findBeforePageResult(search);
		search.setBeginIndex(0);
		search.setRows(10);
        List<BatteryReport> batteryAfterReportList = batteryReportMapper.findAfterPageResult(search);

		page.setTotalItems(beforePageCount + afterPageCount);
		search.setBeginIndex(page.getOffset());
        List<BatteryReport> batteryReportList = new ArrayList<BatteryReport>();
        for (BatteryReport batteryReport : batteryAfterReportList) {
            batteryReportList.add(batteryReport);
        }
		for (BatteryReport batteryReport : batteryBeforeReportList) {
			batteryReportList.add(batteryReport);
		}
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
        return page;
    }

    public List<BatteryReport> findForExcel(BatteryReport search) {
        if (search.getCreateTime() == null) {
            search.setCreateTime(new Date());
        }
        String suffix =dealSuffix(search.getCreateTime());
        search.setSuffix(suffix);
        List<BatteryReport> pageResult = batteryReportMapper.findPageResult(search);
        return pageResult;
    }

    public List<BatteryReport> findList(String batteryId, Date createTime) {
        String suffix =dealSuffix(createTime);
        return batteryReportMapper.findList(batteryId, createTime, suffix);
    }

    public BatteryReport findRecentInfo(String batteryId) {
        Date date = new Date();
        BatteryReport dbBatteryReport = null;
        for (int i = 0; i < 30; i++) {
            String suffix =dealSuffix(date);
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

    private String dealSuffix(Date date){
        String suffix = null;

        //2019-09-10 切换成日报表
        Date subDate = null;
        try {
             subDate = DateUtils.parseDate( "2019-09-10",  new String[] {Constant.DATE_FORMAT});
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date.compareTo(subDate) >= 0){
            suffix = DateFormatUtils.format(date, Constant.DATE_FORMAT_NO_LINE);
        }else {
            suffix = DateFormatUtils.format(date, "yyyyww");
        }

        return suffix;
    }

}
