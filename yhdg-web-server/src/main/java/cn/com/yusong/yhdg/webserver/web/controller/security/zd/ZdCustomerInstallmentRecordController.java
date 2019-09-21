package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zd/zd_customer_installment_record")
public class ZdCustomerInstallmentRecordController extends SecurityController {

    @Autowired
    CustomerInstallmentRecordService customerInstallmentRecordService;

    @SecurityControl(limits = "zd.ZdCustomerInstallmentRecord:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.ZdCustomerInstallmentRecord:list");
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
        return "/security/zd/zd_customer_installment_record/view";
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
        return "/security/zd/zd_customer_installment_record/view_basic_info";
    }
}