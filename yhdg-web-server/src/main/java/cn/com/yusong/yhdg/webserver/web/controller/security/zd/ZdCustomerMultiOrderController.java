package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerMultiOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zd/customer_multi_order")
public class ZdCustomerMultiOrderController extends SecurityController {

    @Autowired
    CustomerMultiOrderService customerMultiOrderService;

    @SecurityControl(limits = "zd.CustomerMultiOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.CustomerMultiOrder:list");
        model.addAttribute("statusEnum", CustomerMultiOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerMultiOrder customerMultiOrder) {
        return PageResult.successResult(customerMultiOrderService.findPage(customerMultiOrder));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        CustomerMultiOrder entity = customerMultiOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        return "/security/zd/customer_multi_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, Long id) {
        CustomerMultiOrder customerMultiOrder = customerMultiOrderService.find(id);
        model.addAttribute("entity", customerMultiOrder);
        model.addAttribute("statusEnum", CustomerMultiOrder.Status.values());
        return "/security/zd/customer_multi_order/view_basic";
    }

    @RequestMapping(value = "view_customer_multi_order_detail.htm")
    public String viewCustomerMultiOrderDetail(Model model, Long orderId) {
        model.addAttribute("orderId", orderId);
        return "/security/zd/customer_multi_order/view_customer_multi_order_detail";
    }

    @RequestMapping(value = "view_customer_multi_pay_detail.htm")
    public String viewCustomerMultiPayDetail(Model model, Long orderId) {
        model.addAttribute("orderId", orderId);
        return "/security/zd/customer_multi_order/view_customer_multi_pay_detail";
    }


}
