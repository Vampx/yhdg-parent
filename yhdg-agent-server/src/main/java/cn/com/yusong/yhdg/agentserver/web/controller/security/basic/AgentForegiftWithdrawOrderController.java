package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftWithdrawOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentForegiftWithdrawOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/agent_foregift_withdraw_order")
public class AgentForegiftWithdrawOrderController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(AgentForegiftWithdrawOrderController.class);

    @Autowired
    AgentForegiftWithdrawOrderService agentForegiftWithdrawOrderService;


    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentForegiftWithdrawOrder search) {
        return PageResult.successResult(agentForegiftWithdrawOrderService.findPage(search));
    }

}
