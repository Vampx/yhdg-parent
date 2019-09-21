package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.LaxinCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/laxin_customer")
public class LaxinCustomerController extends SecurityController {

    @Autowired
    LaxinCustomerService laxinCustomerService;

    @SecurityControl(limits = "basic.LaxinCustomer:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.LaxinCustomer:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(LaxinCustomer search) {
        return PageResult.successResult(laxinCustomerService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(String id, Model model) {
        LaxinCustomer laxinCustomer = laxinCustomerService.find(id);
        if(laxinCustomer == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("id", id);
        return "/security/basic/laxin_customer/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_basic_info.htm")
    public String view_basic_info(String id, Model model) {
        LaxinCustomer laxinCustomer = laxinCustomerService.find(id);
        if(laxinCustomer == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxinCustomer);
        return "/security/basic/laxin_customer/view_basic_info";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("cancel_laxin.htm")
    public String cancel(String id, Model model) {
        LaxinCustomer laxinCustomer = laxinCustomerService.find(id);
        if(laxinCustomer == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("id", id);
        return "/security/basic/laxin_customer/cancel_laxin";
    }

    @RequestMapping("cancel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateCancel(String id,String cancelCanuse) {
        return laxinCustomerService.cancel(id, cancelCanuse);
    }
}
