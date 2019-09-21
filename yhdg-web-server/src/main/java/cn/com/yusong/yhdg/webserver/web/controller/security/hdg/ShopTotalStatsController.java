package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopDayStatsService;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopTotalStatsService;
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
@RequestMapping(value = "/security/hdg/shop_total_stats")
public class ShopTotalStatsController extends SecurityController {

    @Autowired
    ShopTotalStatsService shopTotalStatsService;
    @Autowired
    AgentService agentService;

    static Logger log = LoggerFactory.getLogger(ShopTotalStatsController.class);

    @SecurityControl(limits = "hdg.ShopTotalStats:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        List<Agent> agentList = agentService.findAll();
        model.addAttribute("agentList", agentList);
        model.addAttribute("Category", ConstEnum.Category.values());
        model.addAttribute(MENU_CODE_NAME, "hdg.ShopTotalStats:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopTotalStats search) {
        if (search.getCategory() == null) {
            search.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        }
        return PageResult.successResult(shopTotalStatsService.findPage(search));
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(ShopTotalStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("门店总收入统计", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"门店总收入统计", getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"门店名称", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"所属运营商", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"总收入", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"换电金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"包时段金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"包时段退款金额", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"订单次数", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"包时段数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"退款包时段数量", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"更新时间", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);
            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);

                List<ShopTotalStats> list = shopTotalStatsService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeShopTotalStates(offset, list, sheet);
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
        String fileName = "门店总收入统计.xls";
        downloadSupport(file, request, response, fileName);
    }
}
