package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.constant.RespCode;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.entity.result.RestResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentDepositOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.webserver.service.basic.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/security/basic/agent_deposit_order")
public class AgentDepositOrderController extends SecurityController {

    @Autowired
    private AgentDepositOrderService agentDepositOrderService;
    @Autowired
    private OrderIdService orderIdService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private UserService userService;

    @SecurityControl(limits = "basic.AgentDepositOrder:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AgentDepositOrder:list");
        model.addAttribute("StatusEnum", AgentDepositOrder.Status.values());
    }

    @ResponseBody
    @RequestMapping("page.htm")
    public PageResult page(AgentDepositOrder search) {
        return PageResult.successResult(agentDepositOrderService.findPage(search));
    }

    @RequestMapping("add.htm")
    public void add() {
    }

    @RequestMapping("/view.htm")
    public void view(Model model, String id) {
        AgentDepositOrder agentDepositOrder = agentDepositOrderService.find(id);
        model.addAttribute("entity", agentDepositOrder);
        model.addAttribute("StatusEnum", AgentDepositOrder.Status.values());
    }

    /**
     * 用户充值(公众号)
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_weixin_mp")
    public RestResult createByWeixinMp(HttpSession session, double dMoney, int agentId) {
        int money=(int)(dMoney*100);
        if (money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }
        SessionUser sessionUser = getSessionUser(session);
        User user = userService.find(sessionUser.getId());
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        AgentDepositOrder agentDepositOrder = new AgentDepositOrder();
        agentDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_DEPOSIT_ORDER));
        agentDepositOrder.setPartnerId(agent.getPartnerId());
        agentDepositOrder.setAgentId(agent.getId());
        agentDepositOrder.setAgentName(agent.getAgentName());
        agentDepositOrder.setAgentCode(agent.getAgentCode());
        agentDepositOrder.setMoney(money);
        agentDepositOrder.setStatus(AgentDepositOrder.Status.NOT_PAID.getValue());
        agentDepositOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        agentDepositOrder.setOperator(user.getFullname());
        agentDepositOrder.setCreateTime(new Date());
        agentDepositOrderService.insert(agentDepositOrder);

        return agentDepositOrderService.payByWeixinMp(false,user, agent, agentDepositOrder);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("qrcode.htm")
    public void qrcode(Model model, String url, String id) {
        model.addAttribute("url", url);
        model.addAttribute("id", id);
    }

    @ResponseBody
    @RequestMapping("query_status.htm")
    public AgentDepositOrder queryStatus(String id){
        AgentDepositOrder agentDepositOrder = agentDepositOrderService.find(id);
        return agentDepositOrder;
    }
}