package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiPayDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerMultiPayDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_multi_pay_detail")
public class CustomerMultiPayDetailController extends SecurityController {

    @Autowired
    CustomerMultiPayDetailService customerMultiPayDetailService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerMultiPayDetail customerMultiPayDetail) {
        return PageResult.successResult(customerMultiPayDetailService.findPage(customerMultiPayDetail));
    }


}
