package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentShopBalanceRecordDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentShopBalanceRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/agent_shop_balance_record_detail")
public class AgentShopBalanceRecordDetailController extends SecurityController {

    @Autowired
    AgentShopBalanceRecordDetailService agentShopBalanceRecordDetailService;

    @ViewModel(ViewModel.JSON)
    @ResponseBody
    @RequestMapping(value = "page.htm")
    public PageResult page(AgentShopBalanceRecordDetail search) {
        return PageResult.successResult(agentShopBalanceRecordDetailService.findPage(search));
    }

}
