package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordOrderDetail;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordOrderDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.webserver.service.zd.RentInsuranceOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zd/rent_insurance_order")
public class RentInsuranceOrderController extends SecurityController {
    @Autowired
    private RentInsuranceOrderService rentInsuranceOrderService;
    @Autowired
    CustomerInstallmentRecordOrderDetailService customerInstallmentRecordOrderDetailService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

    @SecurityControl(limits = "zd.RentInsuranceOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.RentInsuranceOrder:list");
        model.addAttribute("StatusEnum", RentInsuranceOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(RentInsuranceOrder search) {
        return PageResult.successResult(rentInsuranceOrderService.findPage(search));
    }

    @RequestMapping("rent_installment.htm")
    public void rentInstallment(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("rent_installment_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult rentInstallmentPage(CustomerInstallmentRecordPayDetail search) {
        CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail = customerInstallmentRecordOrderDetailService.findOrderBySourceId(search.getSourceId(), ConstEnum.Category.RENT.getValue());
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
        RentInsuranceOrder entity = rentInsuranceOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_insurance_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        RentInsuranceOrder entity = rentInsuranceOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
            model.addAttribute("StatusEnum", RentInsuranceOrder.Status.values());
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_insurance_order/view_basic";
    }
}
