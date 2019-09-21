package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerMultiOrderDetailService;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrderDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_multi_order_detail")
public class CustomerMultiOrderDetailController extends SecurityController {

    @Autowired
    CustomerMultiOrderDetailService customerMultiOrderDetailService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerMultiOrderDetail customerMultiOrderDetail) {
        return PageResult.successResult(customerMultiOrderDetailService.findPage(customerMultiOrderDetail));
    }


}
