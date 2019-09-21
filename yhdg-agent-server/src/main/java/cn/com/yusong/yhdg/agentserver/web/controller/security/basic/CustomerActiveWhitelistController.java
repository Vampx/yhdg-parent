
package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerActiveWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_active_whitelist")
public class CustomerActiveWhitelistController extends SecurityController {

    @Autowired
    CustomerActiveWhitelistService customerActiveWhitelistService;

//    @SecurityControl(limits = OperCodeConst.CODE_1_1_6_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_01_06.getValue());
        model.addAttribute("registerTypeEnum", Customer.RegisterType.values());
//        model.addAttribute("typeEnum", Customer.Type.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Customer search) {
        return PageResult.successResult(customerActiveWhitelistService .findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_not_whitelist_customer.htm")
    public void selectNotWhitelistCustomer() {
    }

    @RequestMapping("page_not_whitelist.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageNotWhitellist(Customer search) {
        return PageResult.successResult(customerActiveWhitelistService .findPageNotWhitelist(search));
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id){
        return customerActiveWhitelistService.delete(id);
    }

    @RequestMapping("insert.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult insert(long[] id){
        return customerActiveWhitelistService.insert(id);
    }

}
