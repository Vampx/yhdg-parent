package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_installment_record")
public class CustomerInstallmentRecordController extends SecurityController {

    @Autowired
    CustomerInstallmentRecordService customerInstallmentRecordService;

    @SecurityControl(limits = "basic.CustomerInstallmentRecord:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.CustomerInstallmentRecord:list");
        model.addAttribute("StatusEnum", CustomerInstallmentRecord.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerInstallmentRecord search) {
        return PageResult.successResult(customerInstallmentRecordService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordService.find(id);
        if(customerInstallmentRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", customerInstallmentRecord);
        }
        return "/security/basic/customer_installment_record/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_basic_info.htm")
    public String viewBasicInfo(Model model, Long id) {
        CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordService.find(id);
        if (customerInstallmentRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", customerInstallmentRecord);
            model.addAttribute("StatusEnum", CustomerInstallmentRecord.Status.values());
        }
        return "/security/basic/customer_installment_record/view_basic_info";
    }
}
