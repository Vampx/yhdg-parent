package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.BalanceTransferOrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/balance_transfer_order_log")
public class BalanceTransferOrderLogController extends SecurityController {

    @Autowired
    BalanceTransferOrderLogService balanceTransferOrderLogService;

    @RequestMapping("list.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult list(String orderId) {
        return PageResult.successResult(balanceTransferOrderLogService.findByOrderId(orderId));
    }
}
