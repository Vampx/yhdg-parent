package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetForegiftRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.AgentCabinetForegiftRecordService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security/hdg/agent_cabinet_foregift_record")
public class AgentCabinetForegiftRecordController extends SecurityController{
    @Autowired
    AgentCabinetForegiftRecordService agentCabinetForegiftRecordService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentCabinetForegiftRecord search) {
        return PageResult.successResult(agentCabinetForegiftRecordService.findPage(search));
    }

    @RequestMapping(value = "view_cabinet_foregift.htm")
    public String viewCabinetForegift(Model model, Integer materialDayStatsId) {
        model.addAttribute("materialDayStatsId", materialDayStatsId);
        return "/security/hdg/agent_cabinet_foregift_record/select_cabinet_foregift_record";
    }
}
