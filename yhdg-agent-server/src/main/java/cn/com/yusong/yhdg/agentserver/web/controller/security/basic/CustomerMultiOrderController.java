package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerMultiOrderService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_multi_order")
public class CustomerMultiOrderController extends SecurityController {

    @Autowired
    CustomerMultiOrderService customerMultiOrderService;

    @SecurityControl(limits = "basic.CustomerMultiOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.CustomerMultiOrder:list");
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
        return "/security/basic/customer_multi_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, Long id) {
        CustomerMultiOrder customerMultiOrder = customerMultiOrderService.find(id);
        model.addAttribute("entity", customerMultiOrder);
        model.addAttribute("statusEnum", CustomerMultiOrder.Status.values());
        return "/security/basic/customer_multi_order/view_basic";
    }

    @RequestMapping(value = "view_customer_multi_order_detail.htm")
    public String viewCustomerMultiOrderDetail(Model model, Long orderId) {
        model.addAttribute("orderId", orderId);
        return "/security/basic/customer_multi_order/view_customer_multi_order_detail";
    }

    @RequestMapping(value = "view_customer_multi_pay_detail.htm")
    public String viewCustomerMultiPayDetail(Model model, Long orderId) {
        model.addAttribute("orderId", orderId);
        return "/security/basic/customer_multi_order/view_customer_multi_pay_detail";
    }


}
