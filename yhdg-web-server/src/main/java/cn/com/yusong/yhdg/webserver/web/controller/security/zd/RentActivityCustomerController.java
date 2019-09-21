package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zd.RentActivityCustomer;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.zd.RentActivityCustomerService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zd/rent_activity_customer")
public class RentActivityCustomerController extends SecurityController {
    @Autowired
    private RentActivityCustomerService rentActivityCustomerService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, Long activityId) {
        model.addAttribute("activityId", activityId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(RentActivityCustomer search){
        return PageResult.successResult(rentActivityCustomerService.findPage(search));
    }

}
