package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentSettlement;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/security/basic/agent_settlement")
public class AgentSettlementController extends SecurityController {

    @Autowired
    AgentSettlementService agentSettlementService;

    //@SecurityControl(limits = OperCodeConst.CODE_1_1_21_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("StateEnum", AgentSettlement.State.values());
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_01_04.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentSettlement search) {
        return PageResult.successResult(agentSettlementService.findPage(search));
    }

    @RequestMapping("adopt.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult adopt(int id,HttpSession httpSession) {
        return agentSettlementService.update(id,getSessionUser(httpSession).getLoginName());
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        AgentSettlement entity = agentSettlementService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_settlement/view";
    }
}
