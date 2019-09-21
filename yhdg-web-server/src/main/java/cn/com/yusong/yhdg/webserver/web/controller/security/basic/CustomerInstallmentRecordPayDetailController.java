package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_installment_record_pay_detail")
public class CustomerInstallmentRecordPayDetailController extends SecurityController {

    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, Long recordId) {
        model.addAttribute("recordId", recordId);
        model.addAttribute("StatusEnum", CustomerInstallmentRecordPayDetail.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerInstallmentRecordPayDetail search) {
        return PageResult.successResult(customerInstallmentRecordPayDetailService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = customerInstallmentRecordPayDetailService.find(id);
        if(customerInstallmentRecordPayDetail == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("StatusEnum", CustomerInstallmentRecordPayDetail.Status.values());
            model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
            model.addAttribute("entity", customerInstallmentRecordPayDetail);
        }
        return "/security/basic/customer_installment_record_pay_detail/view";
    }

}
