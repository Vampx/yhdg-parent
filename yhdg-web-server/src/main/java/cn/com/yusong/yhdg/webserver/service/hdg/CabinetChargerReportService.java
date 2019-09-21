package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.GPSUtil;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryReportMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetChargerReportDateMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetChargerReportMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class CabinetChargerReportService {
    @Autowired
    CabinetChargerReportMapper cabinetChargerReportMapper;
    @Autowired
    CabinetChargerReportDateMapper cabinetChargerReportDateMapper;

    public CabinetChargerReport find(String cabinetId, String boxNum, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, "yyyyMMdd");
        CabinetChargerReport cabinetChargerReport = new CabinetChargerReport();
        cabinetChargerReport.setCabinetId(cabinetId);
        cabinetChargerReport.setBoxNum(boxNum);
        cabinetChargerReport.setSuffix(suffix);
        cabinetChargerReport.setCreateTime(createTime);
        return cabinetChargerReportMapper.find(cabinetChargerReport);
    }

    private void makeTableData(CabinetChargerReport search, String suffix, Page page) {
        search.setSuffix(suffix);
        page.setTotalItems(cabinetChargerReportMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetChargerReport> cabinetChargerReportList = cabinetChargerReportMapper.findPageResult(search);
        page.setResult(cabinetChargerReportList);
    }

    public Page findPage(CabinetChargerReport search) throws ParseException {
        Page page = search.buildPage();
        //一开始的请求没有带上时间
        if (search.getCreateTime() == null) {
            search.setCreateTime(new Date());

            String suffix = DateFormatUtils.format(search.getCreateTime(), "yyyyMMdd");
            //查询表是否存在
            if (StringUtils.isNotEmpty(cabinetChargerReportMapper.findTableExist(CabinetChargerReport.CABINET_CHARGER_REPORT_TABLE_NAME + suffix))) {
                makeTableData(search, suffix, page);
            } else {
                //表不存在，查询最近的上报日期
                CabinetChargerReportDate cabinetChargerReportDate = cabinetChargerReportDateMapper.findLast(search.getCabinetId(), search.getBoxNum());
                if (cabinetChargerReportDate != null) {
                    if (StringUtils.isNotEmpty(cabinetChargerReportDate.getReportDate())) {
                        Date date = DateUtils.parseDate(cabinetChargerReportDate.getReportDate(), new String[]{Constant.DATE_FORMAT});
                        search.setCreateTime(date);
                        String dataSuffix = DateFormatUtils.format(date, "yyyyMMdd");
                        if (StringUtils.isNotEmpty(cabinetChargerReportMapper.findTableExist(CabinetChargerReport.CABINET_CHARGER_REPORT_TABLE_NAME + dataSuffix))) {
                            makeTableData(search, dataSuffix, page);
                        }
                    }
                }
            }
        } else {
            //带上时间的查询
            String suffix = DateFormatUtils.format(search.getCreateTime(), "yyyyMMdd");
            //查询表是否存在
            if (StringUtils.isNotEmpty(cabinetChargerReportMapper.findTableExist(CabinetChargerReport.CABINET_CHARGER_REPORT_TABLE_NAME + suffix))) {
                makeTableData(search, suffix, page);
            }
        }

        return page;
    }

    public List<CabinetChargerReport> findForExcel (CabinetChargerReport search) {
        if (search.getCreateTime() == null) {
            search.setCreateTime(new Date());
        }
        String suffix = DateFormatUtils.format(search.getCreateTime(), "yyyyMMdd");
        search.setSuffix(suffix);
        List<CabinetChargerReport> cabinetChargerReportList = cabinetChargerReportMapper.findPageResult(search);
        return cabinetChargerReportList;
    }
}
