package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerGuideService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("api_v1_customer_basic_customer_guide")
@RequestMapping(value = "/api/v1/customer/basic/customer_guide")
public class CustomerGuideController extends ApiController {

    @Autowired
    CustomerGuideService customerGuideService;

    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult list() {
        return customerGuideService.list();
    }
}
