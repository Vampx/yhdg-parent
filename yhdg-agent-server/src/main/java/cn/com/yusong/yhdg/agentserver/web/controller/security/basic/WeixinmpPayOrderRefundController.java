package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinmpPayOrderRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/weixinmp_pay_order_refund")
public class WeixinmpPayOrderRefundController extends SecurityController {
    @Autowired
    WeixinmpPayOrderRefundService weixinmpPayOrderRefundService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(WeixinmpPayOrderRefund search) {
        return PageResult.successResult(weixinmpPayOrderRefundService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_weixinmp_pay_order_refund.htm")
    public String viewWeixinmpPayOrderRefund(Integer partnerId, String statsDate, Model model) {
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("statsDate", statsDate);
        return "/security/basic/partner_in_out_cash/view_weixinmp_pay_order_refund";
    }

}
