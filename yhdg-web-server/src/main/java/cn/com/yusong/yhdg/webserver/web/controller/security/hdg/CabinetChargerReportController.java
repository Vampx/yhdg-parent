package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReport;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetChargerReport;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetChargerReportService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
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
import java.util.*;

/**
 * 换电柜充电器上报日志
 */
@Controller
@RequestMapping(value = "/security/hdg/cabinet_charger_report")
public class CabinetChargerReportController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(CabinetChargerReportController.class);

    @Autowired
    CabinetChargerReportService cabinetChargerReportService;

    @RequestMapping(value = "view.htm")
    public String view(Model model, String cabinetId, String boxNum, Date createTime) {
        CabinetChargerReport entity = cabinetChargerReportService.find(cabinetId, boxNum, createTime);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet_charger_report/view";
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetChargerReport search) throws ParseException {
        return PageResult.successResult(cabinetChargerReportService.findPage(search));
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//导出excel
    public ExtResult exportExcel(CabinetChargerReport search) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            String dateString = "";
            if (search.getCreateTime() != null) {
                dateString = DateFormatUtils.format(search.getCreateTime(), Constant.DATE_FORMAT);
            }
            WritableSheet sheet = wwb.createSheet("充电器" + "上报日志" + dateString, 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0, "柜子" + search.getCabinetId() + "格口" + search.getBoxNum() + "充电器" + "上报日志" + dateString, getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"充电器版本", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"充电器型号", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"充电状态", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"充电阶段", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"充电时长", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"充电器输出电压", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "电池端检测电压", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "充电器输出电流", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"变压器温度", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "散热片温度", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"环境温度", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"充电器故障", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"创建时间", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);
            int offset = 0, limit = 1000;

            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);
                List<CabinetChargerReport> list = cabinetChargerReportService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeCabinetChargerReport(offset, list, sheet);
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
        if (search.getCreateTime() != null) {
            formatDate = DateFormatUtils.format(search.getCreateTime(), "yyyy-MM-dd");
        }
        Object[] objs = {(AppConstant.PATH_TEMP + file.getName()), (formatDate), (search.getCabinetId()), (search.getBoxNum())};
        return DataResult.successResult(objs);
    }

    @RequestMapping("download.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void download(String filePath, String formatDate, String cabinetId, String boxNum, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().appDir, filePath);
        YhdgUtils.makeParentDir(file);
        String fileName = "充电器" + "上报日志" + formatDate + ".xls";
        downloadSupport(file, request, response, fileName);
    }

}
