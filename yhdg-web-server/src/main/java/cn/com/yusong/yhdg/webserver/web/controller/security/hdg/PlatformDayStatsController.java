package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.PlatformDayStatsService;
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
@RequestMapping(value = "/security/hdg/platform_day_stats")
public class PlatformDayStatsController extends SecurityController {

    @Autowired
    PlatformDayStatsService platformDayStatsService;
    @Autowired
    AgentService agentService;

    static Logger log = LoggerFactory.getLogger(PlatformDayStatsController.class);

    @SecurityControl(limits = "hdg.PlatformDayStats:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        List<Agent> agentList = agentService.findAll();
        model.addAttribute("agentList", agentList);
        model.addAttribute(MENU_CODE_NAME, "hdg.PlatformDayStats:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PlatformDayStats search) {
        return PageResult.successResult(platformDayStatsService.findPage(search));
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(PlatformDayStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("平台日收入统计", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"平台日收入统计", getTitleStyle()));


            int column = 0;
            sheet.addCell(new Label(column++, 1,"统计日期", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"运营商总收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总押金收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增押金收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总换电收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增换电收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总包时段收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增包时段收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总充值收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增充值收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总换电单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增换电单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总押金单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增押金单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总充值单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增充值单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总押金退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增押金退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总换电退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增换电退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总包时段退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增包时段退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总充值退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增充值退款", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总换电退款单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增换电退款单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总押金退款单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增押金退款单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总充值退款单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增充值退款单量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总设备数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增设备数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总客户人数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增客户人数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总投诉量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"新增投诉量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"未使用人数统计", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);
            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);

                List<PlatformDayStats> list = platformDayStatsService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writePlatformDayStats(offset, list, sheet);
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
        String fileName = "平台日收入统计.xls";
        downloadSupport(file, request, response, fileName);
    }
}
