package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetRentRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.AgentCabinetRentRecordService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security/hdg/agent_cabinet_rent_record")
public class AgentCabinetRentRecordController extends SecurityController{
    @Autowired
    AgentCabinetRentRecordService agentCabinetRentRecordService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentCabinetRentRecord search) {
        return PageResult.successResult(agentCabinetRentRecordService.findPage(search));
    }

    @RequestMapping(value = "view_cabinet_rent.htm")
    public String viewCabinetForegift(Model model, Integer materialDayStatsId) {
        model.addAttribute("materialDayStatsId", materialDayStatsId);
        return "/security/hdg/agent_cabinet_rent_record/select_cabinet_rent_record";
    }
}
