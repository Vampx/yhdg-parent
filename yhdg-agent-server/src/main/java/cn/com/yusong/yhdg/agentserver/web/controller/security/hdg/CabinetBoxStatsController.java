package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetBoxStatsService;
import cn.com.yusong.yhdg.agentserver.utils.StatsXlsUtils;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
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
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_box_stats")
public class CabinetBoxStatsController extends SecurityController {

    @Autowired
    CabinetBoxStatsService cabinetBoxStatsService;
    @Autowired
    DictItemService dictItemService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_3_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_03_04.getValue());
       // model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Cabinet cabinet) {
        return PageResult.successResult(cabinetBoxStatsService.findPage(cabinet));
    }

    @RequestMapping("battery_state_count.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public DataResult batteryStateCount(Integer agentId, Integer type, Model model) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("batteryCount", cabinetBoxStatsService.statsBoxCountByStatusAndAgent(agentId, type));
        map.put("chargeCount", cabinetBoxStatsService.statsCountByChargeStatusAndAgent(agentId, type));
        map.put("completeCount", cabinetBoxStatsService.statsCompleteChargeCountAndAgent(agentId, type));
        return DataResult.successResult(map);
    }

    @RequestMapping("stats_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult statsPage(CabinetBox cabinetBox) {
        return PageResult.successResult(cabinetBoxStatsService.statsPage(cabinetBox));
    }

    @RequestMapping(value = "view.htm")
    public void viewBox(Model model, String cabinetId, int viewFlag) {
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("viewFlag", viewFlag);
        model.addAttribute("boxStatusEnum", CabinetBox.BoxStatus.values());
    }


    @RequestMapping("export_excel.htm")
    public void exportExcel(Integer agentId, String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"换电柜电池.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        Cabinet cabinet = new Cabinet();
        cabinet.setId(id);
        cabinet.setAgentId(agentId);
        StatsXlsUtils.writeCabinetBattery(cabinetBoxStatsService.findList(cabinet), os);
        downloadSupport(writeFile, request, response, "换电柜电池.xls");
    }

}
