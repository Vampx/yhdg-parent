package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.domain.basic.WithdrawTransferLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.WithdrawTransferLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security/basic/withdraw_transfer_log")
public class WithdrawTransferLogController extends SecurityController {

    @Autowired
    private WithdrawTransferLogService withdrawTransferLogService;

    @ResponseBody
    @RequestMapping("page.htm")
    public PageResult page(WithdrawTransferLog search) {
        return PageResult.successResult(withdrawTransferLogService.findPage(search));
    }

    @RequestMapping("/view.htm")
    public void view(Model model, long id){
        WithdrawTransferLog withdraw = withdrawTransferLogService.find(id);
        model.addAttribute("entity", withdraw);
    }

}