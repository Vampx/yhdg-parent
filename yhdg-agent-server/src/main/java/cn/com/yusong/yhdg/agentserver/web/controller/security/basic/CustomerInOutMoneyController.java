package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInOutMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_in_out_money")
public class CustomerInOutMoneyController extends SecurityController {

    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, int customerId) {
        model.addAttribute("bizTypeEnum", CustomerInOutMoney.BizType.values());
        model.addAttribute("customerId", customerId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerInOutMoney search) {
        return PageResult.successResult(customerInOutMoneyService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        CustomerInOutMoney customerInOutMoney = customerInOutMoneyService.find(id);
        if(customerInOutMoney == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", customerInOutMoney);
        }
        return "/security/basic/customer_in_out_money/view";
    }
}
