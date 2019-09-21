package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.constant.AppConstant;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryReportLogService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 电池上报日志
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_report_log")
public class BatteryReportLogController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(BatteryReportLogController.class);

    @Autowired
    BatteryReportLogService batteryReportLogService;
    @Autowired
    BatteryService batteryService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_6_3_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_06_03.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryReportLog search) {
        return PageResult.successResult(batteryReportLogService.findPage(search));
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//导出excel
    public ExtResult exportExcel(BatteryReportLog search) throws IOException {
        BatteryReportLog.BatteryReportLogColumn[] columns = BatteryReportLog.BatteryReportLogColumn.values();
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            String dateString = "";
            if (search.getReportTime() != null) {
                dateString = DateFormatUtils.format(search.getReportTime(), Constant.DATE_FORMAT);
            }
            WritableSheet sheet = wwb.createSheet("电池" + search.getBatteryId() + "上报日志" + dateString, 0);
            sheet.getSettings().setDefaultColumnWidth(columns.length - 2);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0, "电池" + search.getBatteryId() + "上报日志" + dateString, getTitleStyle()));
            sheet.mergeCells(0, 0, columns.length - 1, 0);
            int i =0;
            for (BatteryReportLog.BatteryReportLogColumn column : columns) {
                sheet.addCell(new Label(i++, 1, column.getName(), getHeadStyle()));
            }

            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);
                List<BatteryReportLog> list = batteryReportLogService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeBatteryReportLogBycolumns(offset, list, sheet);
                offset += limit;
            }
            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();
        } catch (Exception e) {
            log.error("upload_excel error", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        String formatDate = "";
        if (search.getReportTime() != null) {
            formatDate = DateFormatUtils.format(search.getReportTime(), "yyyy-MM-dd");
        }
        Object[] objs = {(AppConstant.PATH_TEMP + file.getName()), (formatDate), (search.getBatteryId())};
        return DataResult.successResult(objs);
    }

    @RequestMapping("download.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void download(String filePath, String formatDate, String batteryId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().appDir, filePath);
        YhdgUtils.makeParentDir(file);
        String fileName = "电池" + batteryId + "上报日志明细" + formatDate + ".xls";
        downloadSupport(file, request, response, fileName);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String batteryId, Date reportTime) {
        BatteryReportLog entity = batteryReportLogService.find(batteryId, reportTime);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_report_log/view";
    }

    @RequestMapping(value = "view_voltage_electricity.htm")
    public String viewVoltageElectricity(Model model, String batteryId, String formatDate) {
        model.addAttribute("batteryId", batteryId);

        Date reportTime = null;
        try {
            reportTime = new SimpleDateFormat(Constant.DATE_TIME_FORMAT).parse(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<BatteryReportLog> list = batteryReportLogService.findList(batteryId, reportTime);
        List<Integer> voltageList = new ArrayList<Integer>();
        List<Integer> electricityList = new ArrayList<Integer>();
        List<Date> timeList = new ArrayList<Date>();
        for (BatteryReportLog batteryReportLog : list) {
            if (batteryReportLog.getVoltage() != null && batteryReportLog.getElectricity() != null) {
                voltageList.add(batteryReportLog.getVoltage());
                electricityList.add(batteryReportLog.getElectricity());
                timeList.add(batteryReportLog.getReportTime());
            }
        }
        if (voltageList.size() == 0 && electricityList.size() == 0) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("oneDay", DateFormatUtils.format(reportTime, Constant.DATE_FORMAT));
        model.addAttribute("batteryId", batteryId);
        model.addAttribute("voltageList", voltageList);
        model.addAttribute("electricityList", electricityList);
        model.addAttribute("timeList", timeList);
        return "/security/hdg/battery_report_log/view_voltage_electricity";
    }

    @RequestMapping(value = "view_temp.htm")
    public String viewTemp(Model model, String batteryId, String formatDate) {
        model.addAttribute("batteryId", batteryId);

        Date reportTime = null;
        try {
            reportTime = new SimpleDateFormat(Constant.DATE_TIME_FORMAT).parse(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<BatteryReportLog> list = batteryReportLogService.findList(batteryId, reportTime);
        if (list.size() == 0) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        String newTemp = null;
        for (BatteryReportLog batteryReportLog : list) {
            if (batteryReportLog.getTemp() != null) {
                newTemp = batteryReportLog.getTemp();
                break;
            }
        }

        String[] newSplit = StringUtils.split(newTemp, ",");
        Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
        for (int i = 0; i < newSplit.length; i++) {
            List<Double> tempList = new ArrayList<Double>();
            map.put(i, tempList);
        }

        List<BatteryReportLog> listReport = new ArrayList<BatteryReportLog>();

        for (BatteryReportLog batteryReportLog : list) {
            String temp = batteryReportLog.getTemp();
            if (temp != null && !temp.equals("")) {

                String[] split = StringUtils.split(temp, ",");

                for (int i = 0; i < split.length; i++) {
                    map.get(i).add(Double.parseDouble(split[i]));
                }
                listReport.add(batteryReportLog);
            }
        }

        model.addAttribute("oneDay", DateFormatUtils.format(reportTime, Constant.DATE_FORMAT));
        for (int i = 0; i < newTemp.length(); i++) {
            model.addAttribute("tempList" + String.valueOf(i), map.get(i));
        }
        List<Integer> numList = new ArrayList<Integer>();
        for (int i = 0; i < newSplit.length; i++) {
            numList.add(i);
        }
        List<List<Double>> newTempList = new ArrayList<List<Double>>();
        for (int i = 0; i < newSplit.length; i++) {
            newTempList.add(map.get(i));
        }
        model.addAttribute("batteryId", batteryId);
        model.addAttribute("numList", numList);
        model.addAttribute("listReport", listReport);
        model.addAttribute("newTempList", newTempList);
        return "/security/hdg/battery_report_log/view_temp";
    }

    @RequestMapping(value = "show_line_chart.htm")
    public String showLineChart(Model model, String batteryId, Date reportTime) {
        model.addAttribute("batteryId", batteryId);
        if (reportTime == null) {
            reportTime = new Date();
        }
        String formatDate = DateFormatUtils.format(reportTime, Constant.DATE_TIME_FORMAT);
        model.addAttribute("formatDate", formatDate);
        return "/security/hdg/battery_report_log/show_line_chart";
    }

}
