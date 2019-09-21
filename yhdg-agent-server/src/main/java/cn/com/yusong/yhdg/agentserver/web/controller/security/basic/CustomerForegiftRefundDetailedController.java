package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftRefundDetailed;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerForegiftRefundDetailedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_foregift_refund_detailed")
public class CustomerForegiftRefundDetailedController extends SecurityController {
    @Autowired
    CustomerForegiftRefundDetailedService customerForegiftRefundDetailedService;

    @RequestMapping(value = "index.htm")
    public void index(Model model,String id) {
        model.addAttribute("id", id);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerForegiftRefundDetailed search) {
        return PageResult.successResult(customerForegiftRefundDetailedService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, String id,int num) {
        CustomerForegiftRefundDetailed entity = customerForegiftRefundDetailedService.find(id,num);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/customer_foregift_refund_detailed/view";
    }

}
