package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.service.basic.AgentInOutMoneyService;
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
@RequestMapping(value = "/security/basic/agent_in_out_money")
public class AgentInOutMoneyController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(AgentInOutMoneyController.class);

    @Autowired
    AgentInOutMoneyService agentInOutMoneyService;

//    @SecurityControl(limits = OperCodeConst.CODE_1_3_1_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer agentId) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_03_01.getValue());
        model.addAttribute("bizTypeEnum", AgentInOutMoney.BizType.values());
        model.addAttribute("typeEnum", AgentInOutMoney.Type.values());
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentInOutMoney search) {
        return PageResult.successResult(agentInOutMoneyService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        AgentInOutMoney agentInOutMoney = agentInOutMoneyService.find(id);
        model.addAttribute("entity", agentInOutMoney);
        return "/security/basic/agent_in_out_money/view";
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(AgentInOutMoney search) throws IOException {
        AgentInOutMoney.AgentInOutMoneyColumn[] columns = AgentInOutMoney.AgentInOutMoneyColumn.values();
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("运营商账单", 0);
            sheet.getSettings().setDefaultColumnWidth(columns.length - 2);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0, "运营商账单", getTitleStyle()));
            sheet.mergeCells(0, 0, columns.length - 1, 0);
            int i =0;
            for (AgentInOutMoney.AgentInOutMoneyColumn column : columns) {
                sheet.addCell(new Label(i++, 1, column.getName(), getHeadStyle()));
            }

            int offset = 0, limit = 1000;
            while (true) {
                search.setBeginIndex(offset);
                search.setRows(limit);
                List<AgentInOutMoney> list = agentInOutMoneyService.findForExcel(search);
                if (list.isEmpty()) {
                    break;
                }
                ExportXlsUtils.writeAgentInOutMoneyBycolumns(offset, list, sheet);
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
        return DataResult.successResult((Object) (AppConstant.PATH_TEMP + file.getName()));
    }

    @RequestMapping("download.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().appDir, filePath);
        YhdgUtils.makeParentDir(file);
        String fileName = "运营商账单明细.xls";
        downloadSupport(file, request, response, fileName);
    }

}
