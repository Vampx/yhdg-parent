package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetDayStatsService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetTotalStatsService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
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
import java.util.List;


@Controller
@RequestMapping(value = "/security/hdg/cabinet_total_stats")
public class CabinetTotalStatsController extends SecurityController {

    @Autowired
    CabinetTotalStatsService cabinetTotalStatsService;
    @Autowired
    AgentService agentService;

    static Logger log = LoggerFactory.getLogger(CabinetTotalStatsController.class);

    @SecurityControl(limits = "hdg.CabinetTotalStats:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        List<Agent> agentList = agentService.findAll();
        model.addAttribute("agentList", agentList);
        model.addAttribute(MENU_CODE_NAME, "hdg.CabinetTotalStats:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetTotalStats search) {
        return PageResult.successResult(cabinetTotalStatsService.findPage(search));
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(CabinetTotalStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("换电柜总收入统计", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"换电柜总收入统计", getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"运营商名称", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电柜编号", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电柜名称", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日总金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"押金金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"押金退款金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "包时段金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "包时段退款金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1, "押金数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"包时段数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"订单次数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"用电量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"单价", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"用电费用", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"更新时间", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);
            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
               search.setRows(limit);

                List<CabinetTotalStats> list = cabinetTotalStatsService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeCabinetTotalStates(offset, list, sheet);
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
        String fileName = "换电柜总收入统计.xls";
        downloadSupport(file, request, response, fileName);
    }
}
