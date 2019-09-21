package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentShopBalanceRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentShopBalanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/agent_shop_balance_record")
public class AgentShopBalanceRecordController extends SecurityController {

    @Autowired
    AgentShopBalanceRecordService agentShopBalanceRecordService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentShopBalanceRecord search) {
        return PageResult.successResult(agentShopBalanceRecordService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_agent_shop_balance_record.htm")
    public String viewAgentShopBalanceRecord(Model model, long id) {
        model.addAttribute("id", id);
        return "/security/basic/agent_agent/view_agent_shop_balance_record_detail";
    }

}
