package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/agent_foregift")
public class AgentForegiftController extends SecurityController {

    @Autowired
    AgentService agentService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;


    @SecurityControl(limits = "basic.AgentForegift:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpServletRequest request) {
        model.addAttribute(MENU_CODE_NAME, "basic.AgentForegift:list");
        model.addAttribute("Category", ConstEnum.Category.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Agent search) {
        if (search.getCategory() == null) {
            search.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        }
        return PageResult.successResult(agentService.findForegiftPage(search));
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, int id, int category) {
        model.addAttribute("id", id);
        model.addAttribute("category", category);
        return "/security/basic/agent_foregift/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_basic_info.htm")
    public String viewBasicInfo(Model model, int id, int category) {
        Agent entity = agentService.findForegift(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("category", category);
        }
        return "/security/basic/agent_foregift/view_basic_info";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_foregift_deposit_order.htm")
    public String viewForegiftOrder(Model model, int id, int category) {
        model.addAttribute("id", id);
        model.addAttribute("category", category);
        return "/security/basic/agent_foregift/view_foregift_deposit_order";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_foregift_withdraw_order.htm")
    public String viewForegiftWithdrawOrder(Model model, int id, int category) {
        model.addAttribute("id", id);
        model.addAttribute("category", category);
        return "/security/basic/agent_foregift/view_foregift_withdraw_order";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_foregift_in_out_money.htm")
    public String viewForegiftInOutMoney(Model model, int id, int category) {
        model.addAttribute("id", id);
        model.addAttribute("category", category);
        return "/security/basic/agent_foregift/view_foregift_in_out_money";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "exchange_withdrawal.htm")
    public String exchangeWithdrawal(Model model, int id) {
        Agent entity = agentService.findForegift(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_foregift/exchange_withdrawal";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "rent_withdrawal.htm")
    public String rentWithdrawal(Model model, int id) {
        Agent entity = agentService.findForegift(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_foregift/rent_withdrawal";
    }

    @ResponseBody
    @RequestMapping("update_exchange_withdrawal.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateExchangeWithdrawal(int id, int money, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        return  agentService.updteExchangeWithdrawal(id, money, userName);
    }

    @ResponseBody
    @RequestMapping("update_rent_withdrawal.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateRentWithdrawal(int id, int money, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        return  agentService.updteRentWithdrawal(id, money, userName);
    }

}
