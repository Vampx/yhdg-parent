package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chen on 2017/10/28.
 */
@Controller
@RequestMapping(value = "/security/basic/alipay_pay_order")
public class AlipayPayOrderController extends SecurityController {
    @Autowired
    AlipayPayOrderService alipayPayOrderService;

    @SecurityControl(limits = "basic.AlipayPayOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AlipayPayOrder:list");
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AlipayPayOrder search) {
        return PageResult.successResult(alipayPayOrderService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(AlipayPayOrder search, Model model) {
        AlipayPayOrder alipayPayOrder = alipayPayOrderService.find(search);
        if(alipayPayOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
        model.addAttribute("entity", alipayPayOrder);
        return "/security/basic/alipay_pay_order/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_alipay_pay_order.htm")
    public String viewAlipayPayOrder(Integer partnerId, String statsDate, Model model) {
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("statsDate", statsDate);
        return "/security/basic/partner_in_out_cash/view_alipay_pay_order";
    }
}
