package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zd/zd_customer_installment_record_pay_detail")
public class ZdCustomerInstallmentRecordPayDetailController extends SecurityController {

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
            model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
            model.addAttribute("StatusEnum", CustomerInstallmentRecordPayDetail.Status.values());
            model.addAttribute("entity", customerInstallmentRecordPayDetail);
        }
        return "/security/zd/zd_customer_installment_record_pay_detail/view";
    }

}
