package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.hdg.AgentDayStatsService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
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
import java.util.List;


@Controller
@RequestMapping(value = "/security/hdg/agent_day_stats")
public class AgentDayStatsController extends SecurityController {

    @Autowired
    AgentDayStatsService agentDayStatsService;
    @Autowired
    AgentService agentService;

    static Logger log = LoggerFactory.getLogger(AgentDayStatsController.class);

    @SecurityControl(limits = "hdg.AgentDayStats:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        List<Agent> agentList = agentService.findAll();
        model.addAttribute("agentList", agentList);
        model.addAttribute("Category", ConstEnum.Category.values());
        model.addAttribute(MENU_CODE_NAME, "hdg.AgentDayStats:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentDayStats search) {
        if (search.getCategory() == null) {
            search.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        }
        return PageResult.successResult(agentDayStatsService.findPage(search));
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(AgentDayStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("运营商日收入统计", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"运营商日收入统计", getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"统计日期", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"运营商名称", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"平台收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"运营商收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"省代收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"市代收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"门店收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"押金剩余金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"抵扣券金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日换电金额(分成前)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日包时段订单(分成前)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日退款换电金额(分成前)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"退款包时段订单金额(分成前)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日换电金额(分成后)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日包时段订单(分成后)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日退款换电金额(分成后)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"退款包时段订单金额(分成后)", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日门店总金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日门店按次金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日门店包时段订单金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日门店包时段退款订单金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日新增换电订单数（换电次数）", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日新增包时段订单数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日新增退款换电订单数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"购买包时段订单次数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"当日新增退款包时段订单次数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"押金金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"押金数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"押金退款金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"押金退款数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"保险金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"保险数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"保险退款金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"保险退款数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"电量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"电费", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"设备数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"电池数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"活跃客户数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"更新时间", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);
            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);

                List<AgentDayStats> list = agentDayStatsService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeAgentDayStates(offset, list, sheet);
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
        String fileName = "运营商日收入统计.xls";
        downloadSupport(file, request, response, fileName);
    }
}
