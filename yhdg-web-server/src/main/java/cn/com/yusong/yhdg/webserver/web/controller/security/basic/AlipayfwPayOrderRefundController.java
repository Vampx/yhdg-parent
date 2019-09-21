package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayfwPayOrderRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/alipayfw_pay_order_refund")
public class AlipayfwPayOrderRefundController extends SecurityController {
    @Autowired
    AlipayfwPayOrderRefundService alipayfwPayOrderRefundService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AlipayfwPayOrderRefund search) {
        return PageResult.successResult(alipayfwPayOrderRefundService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_alipayfw_pay_order_refund.htm")
    public String viewAlipayfwPayOrderRefund(Integer partnerId, String statsDate, Model model) {
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("statsDate", statsDate);
        return "/security/basic/partner_in_out_cash/view_alipayfw_pay_order_refund";
    }



}
