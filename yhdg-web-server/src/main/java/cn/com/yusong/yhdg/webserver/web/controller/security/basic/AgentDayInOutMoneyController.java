package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentDayInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentDayInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.hdg.AgentDayStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/agent_day_in_out_money")
public class AgentDayInOutMoneyController extends SecurityController {

    @Autowired
    AgentDayInOutMoneyService agentDayInOutMoneyService;

    @SecurityControl(limits = "basic.AgentDayInOutMoney:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AgentDayInOutMoney:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentDayInOutMoney search) {
        return PageResult.successResult(agentDayInOutMoneyService.findPage(search));
    }

    @RequestMapping("view_agent_in_money.htm")
    public String viewAgentInMoney(Model model, Integer agentId, String statsDate) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("statsDate", statsDate);
        return "security/basic/agent_day_in_out_money/view_agent_in_money";
    }

    @RequestMapping("view_agent_out_money.htm")
    public String viewAgentOutMoney(Model model, Integer agentId, String statsDate) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("statsDate", statsDate);
        return "security/basic/agent_day_in_out_money/view_agent_out_money";
    }

}
