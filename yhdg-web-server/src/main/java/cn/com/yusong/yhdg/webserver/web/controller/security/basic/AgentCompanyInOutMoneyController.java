package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentCompanyInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.ShopInOutMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/agent_company_in_out_money")
public class AgentCompanyInOutMoneyController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(AgentCompanyInOutMoneyController.class);

    @Autowired
    AgentCompanyInOutMoneyService agentCompanyInOutMoneyService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, String agentCompanyId) {
        model.addAttribute("bizTypeEnum", AgentCompanyInOutMoney.BizType.values());
        model.addAttribute("typeEnum", AgentCompanyInOutMoney.Type.values());
        model.addAttribute("agentCompanyId", agentCompanyId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentCompanyInOutMoney search) {
        return PageResult.successResult(agentCompanyInOutMoneyService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        AgentCompanyInOutMoney agentCompanyInOutMoney = agentCompanyInOutMoneyService.find(id);
        model.addAttribute("entity", agentCompanyInOutMoney);
        return "/security/basic/agent_company_in_out_money/view";
    }

    @RequestMapping(value = "alert_page.htm")
    public void alertPage(Model model, String agentCompanyId) {
        model.addAttribute("agentCompanyId", agentCompanyId);
        model.addAttribute("typeEnum", AgentCompanyInOutMoney.Type.values());
    }

}
