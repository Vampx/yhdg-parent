package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetOnlineStats;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetOnlineStatsService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_online_stats")
public class CabinetOnlineStatsController extends SecurityController {
    @Autowired
    CabinetOnlineStatsService cabinetOnlineStatsService;

    @RequestMapping(value = "index.htm")
    public void index(String cabinetId, Model model) {
        model.addAttribute("cabinetId", cabinetId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetOnlineStats search) {
        return PageResult.successResult(cabinetOnlineStatsService.findPage(search));
    }

}
