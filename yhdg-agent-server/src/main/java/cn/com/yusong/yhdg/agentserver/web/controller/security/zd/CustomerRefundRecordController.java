package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerRefundRecordService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("zd_customer_refund_record")
@RequestMapping(value = "/security/zd/customer_refund_record")
public class CustomerRefundRecordController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(CustomerRefundRecordController.class);

    @Autowired
    CustomerRefundRecordService customerRefundRecordService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, CustomerRefundRecord entity) {
        model.addAttribute("entity", entity);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerRefundRecord search) {
        search.setType(CustomerRefundRecord.Type.ZD.getValue());
        return PageResult.successResult(customerRefundRecordService.findPage(search));
    }

    @RequestMapping(value = "view_record.htm")
    public String viewRecord(Model model, Long id) {
        CustomerRefundRecord entity = customerRefundRecordService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        return "/security/zd/customer_refund_record/view_record";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String sourceId, Integer sourceType) {
        model.addAttribute("sourceId", sourceId);
        model.addAttribute("sourceType", sourceType);
        return "/security/zd/customer_refund_record/view";
    }


}
