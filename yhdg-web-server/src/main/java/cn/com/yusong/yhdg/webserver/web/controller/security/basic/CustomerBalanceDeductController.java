package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.CustomerBalanceDeduct;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerBalanceDeductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/security/basic/customer_balance_deduct")
public class CustomerBalanceDeductController extends SecurityController {
    @Autowired
    CustomerBalanceDeductService customerBalanceDeductService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpServletRequest request) {
       // model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_02_12.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerBalanceDeduct search) {
        return PageResult.successResult(customerBalanceDeductService.findPage(search));
    }


}
