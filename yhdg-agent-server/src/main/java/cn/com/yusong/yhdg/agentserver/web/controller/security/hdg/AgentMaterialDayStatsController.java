package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.AgentMaterialDayStatsService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
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
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/security/hdg/agent_material_day_stats")
public class AgentMaterialDayStatsController extends SecurityController{

    @Autowired
    AgentMaterialDayStatsService agentMaterialDayStatsService;

    static Logger log = LoggerFactory.getLogger(AgentMaterialDayStatsController.class);

    @SecurityControl(limits = "hdg.AgentMaterialDayStats:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("StatusEnum", AgentMaterialDayStats.Status.values());
        model.addAttribute("Category", ConstEnum.Category.values());
        model.addAttribute(MENU_CODE_NAME, "hdg.AgentMaterialDayStats:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentMaterialDayStats search) {
        if (search.getCategory() == null) {
            search.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        }
        return PageResult.successResult(agentMaterialDayStatsService.findPage(search));
    }

    @RequestMapping("pay_money.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult payMoney(int[] ids, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        return agentMaterialDayStatsService.payMoney(ids, sessionUser.getUsername());
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(AgentMaterialDayStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("运营商设备支出统计", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"运营商设备支出统计", getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"所属运营商", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"结算日期", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"设备押金数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"设备押金金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"设备租金数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"设备租金金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"电池租金数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"电池租金金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"客户认证数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"客户认证金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"状态", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"支付时间", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"创建时间", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);
            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);

                List<AgentMaterialDayStats> list = agentMaterialDayStatsService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeAgentMaterialDayStats(offset, list, sheet);
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
        String fileName = "运营商设备支出统计.xls";
        downloadSupport(file, request, response, fileName);
    }

}
