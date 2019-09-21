package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayfwPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping(value = "/security/basic/alipayfw_pay_order")
public class AlipayfwPayOrderController extends SecurityController {
    @Autowired
    AlipayfwPayOrderService alipayfwPayOrderService;

    @SecurityControl(limits = "basic.AlipayfwPayOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AlipayfwPayOrder:list");
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AlipayfwPayOrder search) {
        return PageResult.successResult(alipayfwPayOrderService.findPage(search));
    }

    @RequestMapping("find_list.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult findList(String mobile){
        List<AlipayfwPayOrder> list = alipayfwPayOrderService.findList(mobile);
        return PageResult.successResult(list);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(String id, Model model) {
        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.find(id);
        if(alipayfwPayOrderService == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
        model.addAttribute("entity", alipayfwPayOrder);
        return "/security/basic/alipayfw_pay_order/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_alipayfw_pay_order.htm")
    public String viewAlipayfwPayOrder(Integer partnerId, String statsDate, Model model) {
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("statsDate", statsDate);
        return "/security/basic/partner_in_out_cash/view_alipayfw_pay_order";
    }

}
