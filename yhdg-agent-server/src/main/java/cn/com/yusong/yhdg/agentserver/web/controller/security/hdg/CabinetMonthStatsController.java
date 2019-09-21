package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetMonthStatsService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
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
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = "/security/hdg/cabinet_month_stats")
public class CabinetMonthStatsController extends SecurityController {

    @Autowired
    CabinetMonthStatsService cabinetMonthStatsService;
    @Autowired
    AgentService agentService;

    static Logger log = LoggerFactory.getLogger(CabinetMonthStatsController.class);

    @SecurityControl(limits = "hdg.CabinetMonthStats:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        List<Agent> agentList = agentService.findAll();
        List<String> yearList = Arrays.asList("2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027");
        List<String> monthList = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        model.addAttribute("yearList", yearList);
        model.addAttribute("monthList", monthList);
        model.addAttribute("agentList", agentList);
        model.addAttribute(MENU_CODE_NAME, "hdg.CabinetMonthStats:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetMonthStats search) {
        return PageResult.successResult(cabinetMonthStatsService.findPage(search));
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(CabinetMonthStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("换电柜月收入统计", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"换电柜月收入统计", getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"运营商名称", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电柜编号", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电柜名称", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"统计日期", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总入账", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "包时段金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "平台分成收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"运营商收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"省代收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"市代收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"订单总单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"包时段单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"退款金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"退款单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"活跃客户数", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);
            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);

                List<CabinetMonthStats> list = cabinetMonthStatsService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeCabinetMonthStates(offset, list, sheet);
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
        return DataResult.successResult((Object) ("/static/temp/" + file.getName()));
    }

    @RequestMapping("download.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().appDir, filePath);
        YhdgUtils.makeParentDir(file);
        String fileName = "换电柜月收入统计.xls";
        downloadSupport(file, request, response, fileName);
    }
}
