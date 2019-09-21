package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.LaxinPayOrderService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/laxin_pay_order")
public class LaxinPayOrderController extends SecurityController {
    @Autowired
    LaxinPayOrderService laxinPayOrderService;

    @SecurityControl(limits = "basic.LaxinPayOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("StatusEnum", LaxinPayOrder.Status.values());
        model.addAttribute(MENU_CODE_NAME, "basic.LaxinPayOrder:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(LaxinPayOrder search) {
        return PageResult.successResult(laxinPayOrderService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(String id, Model model) {
        LaxinPayOrder laxinPayOrder = laxinPayOrderService.find(id);
        if(laxinPayOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("id", id);
        return "/security/basic/laxin_pay_order/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_basic_info.htm")
    public String view_basic_info(String id, Model model) {
        LaxinPayOrder laxinPayOrder = laxinPayOrderService.find(id);
        if(laxinPayOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxinPayOrder);
        return "/security/basic/laxin_pay_order/view_basic_info";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_laxin_record.htm")
    public String view_laxin_record(String id, Model model) {
        LaxinPayOrder laxinPayOrder = laxinPayOrderService.find(id);
        if(laxinPayOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxinPayOrder);
        return "/security/basic/laxin_pay_order/view_laxin_record";
    }
}
