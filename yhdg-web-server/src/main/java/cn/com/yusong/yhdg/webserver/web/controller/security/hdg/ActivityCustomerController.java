package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ActivityCustomer;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.ActivityCustomerService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 活动客户
 */
@Controller
@RequestMapping(value = "/security/hdg/activity_customer")
public class ActivityCustomerController extends SecurityController {

    @Autowired
    ActivityCustomerService activityCustomerService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, Long activityId) {
        model.addAttribute("activityId", activityId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ActivityCustomer search){
        return PageResult.successResult(activityCustomerService.findPage(search));
    }


}
