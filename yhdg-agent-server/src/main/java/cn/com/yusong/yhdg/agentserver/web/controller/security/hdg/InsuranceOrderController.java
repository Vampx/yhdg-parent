package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordOrderDetailService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.agentserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordOrderDetail;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/insurance_order")
public class InsuranceOrderController extends SecurityController{
    @Autowired
    InsuranceOrderService insuranceOrderService;
    @Autowired
    CustomerInstallmentRecordOrderDetailService customerInstallmentRecordOrderDetailService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

    @SecurityControl(limits = "hdg.InsuranceOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.InsuranceOrder:list");
        model.addAttribute("StatusEnum", InsuranceOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(InsuranceOrder search) {
        return PageResult.successResult(insuranceOrderService.findPage(search));
    }

    @RequestMapping("exchange_installment.htm")
    public void exchangeInstallment(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("exchange_installment_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult exchangeInstallmentPage(CustomerInstallmentRecordPayDetail search) {
        CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail = customerInstallmentRecordOrderDetailService.findOrderBySourceId(search.getSourceId(), ConstEnum.Category.EXCHANGE.getValue());
        if (customerInstallmentRecordOrderDetail != null) {
            search.setRecordId(customerInstallmentRecordOrderDetail.getRecordId());
            return PageResult.successResult(customerInstallmentRecordPayDetailService.findPage(search));
        }else{
            search.setRecordId(-1L);
            return PageResult.successResult(customerInstallmentRecordPayDetailService.findPage(search));
        }
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        InsuranceOrder entity = insuranceOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/insurance_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        InsuranceOrder entity = insuranceOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
            model.addAttribute("StatusEnum", InsuranceOrder.Status.values());
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/insurance_order/view_basic";
    }
}
