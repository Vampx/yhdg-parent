package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetBatteryStatsService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryStats;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_battery_stats")
public class CabinetBatteryStatsController extends SecurityController {

    @Autowired
    CabinetBatteryStatsService cabinetBatteryStatsService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetBatteryStats search) {
        return PageResult.successResult(cabinetBatteryStatsService.findPage(search));
    }

    @RequestMapping(value = "/view.htm")
    public String selectCabinetBatteryStats(Model model, Long id) {
        CabinetBatteryStats cabinetBatteryStats = cabinetBatteryStatsService.find(id);
        model.addAttribute("entity", cabinetBatteryStats);
        return "/security/hdg/cabinet_battery_stats/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "/select_cabinet_battery_stats.htm")
    public void selectCabinetBatteryStats(Model model) {
        model.addAttribute("StatusEnum", CabinetBatteryStats.Status.values());
    }
}
