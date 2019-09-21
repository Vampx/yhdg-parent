package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCustomer;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentCustomerService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentSettingService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/security/hdg/exchange_installment_customer")
public class ExchangeInstallmentCustomerController extends SecurityController {

    @Autowired
    CustomerService customerService;
    @Autowired
    ExchangeInstallmentCustomerService exchangeInstallmentCustomerService;
    @Autowired
    ExchangeInstallmentSettingService exchangeInstallmentSettingService;


    @SecurityControl(limits = "hdg.ExchangeInstallmentCustomer:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, String settingId) {
        model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeInstallmentCustomer:list");
        model.addAttribute("settingId", settingId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ExchangeInstallmentCustomer search) {
        return PageResult.successResult(exchangeInstallmentCustomerService.findPage(search));
    }

    @RequestMapping("add_installment_customer_mobile")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult add_installment_customer_mobile(Model model, String mobile ,Long settingId){
        int insert = 0;
        if(StringUtils.isEmpty(mobile)){
            return ExtResult.failResult("手机号不能为空");
        }
        Customer byMobile = customerService.findByMobile(mobile);
        ExchangeInstallmentCustomer exchangeInstallmentCustomer = new ExchangeInstallmentCustomer();
        if(byMobile!=null){
            exchangeInstallmentCustomer.setCustomerId(byMobile.getId());
            exchangeInstallmentCustomer.setCustomerFullname(byMobile.getFullname());
        }
        exchangeInstallmentCustomer.setSettingId(settingId);
        exchangeInstallmentCustomer.setCustomerMobile(mobile);
        ExchangeInstallmentCustomer customerMobile = exchangeInstallmentCustomerService.findCustomerMobile(exchangeInstallmentCustomer);
        if(customerMobile !=null){
            return  ExtResult.failResult("此骑手已绑定其他分期设置！");
        }
        if(settingId!=null){
            ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingService.find(settingId);
            if(exchangeInstallmentSetting==null){
                ExtResult.failResult("分期设置ID不能为空！");
            }
            insert = exchangeInstallmentCustomerService.insert(exchangeInstallmentCustomer);
        }
        if(insert==0&&settingId != null){
            return ExtResult.failResult("绑定骑手失败！");
        }else{
            return DataResult.successResult(byMobile);
        }

    }

    @RequestMapping("delect_installment_customer_mobile")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delectInstallmentCustomerMobile(Model model, String customerMobile,Long settingId){
        return exchangeInstallmentCustomerService.deleteCustomerMobile(customerMobile,settingId);
    }

}
