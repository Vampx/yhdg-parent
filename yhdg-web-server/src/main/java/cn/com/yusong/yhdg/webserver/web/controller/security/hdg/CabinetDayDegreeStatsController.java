package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetDayDegreeStatsService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security/hdg/cabinet_day_degree_stats")
public class CabinetDayDegreeStatsController extends SecurityController {
    @Autowired
    CabinetDayDegreeStatsService cabinetDayDegreeStatsService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetDayDegreeStats entity) {
        return PageResult.successResult(cabinetDayDegreeStatsService.findPage(entity));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "/select_cabinet_day_degree_stats.htm")
    public void selectCabinetDayDegreeStats(Model model) {

    }
}
